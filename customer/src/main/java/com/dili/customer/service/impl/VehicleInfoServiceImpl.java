package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.assets.sdk.dto.CarTypeDTO;
import com.dili.customer.commons.service.CarTypeRpcService;
import com.dili.customer.domain.VehicleInfo;
import com.dili.customer.domain.dto.VehicleInfoDto;
import com.dili.customer.mapper.VehicleInfoMapper;
import com.dili.customer.service.VehicleInfoService;
import com.dili.ss.base.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/11/29 11:07
 */
@RequiredArgsConstructor
@Service
public class VehicleInfoServiceImpl extends BaseServiceImpl<VehicleInfo, Long> implements VehicleInfoService {

    public VehicleInfoMapper getActualMapper() {
        return (VehicleInfoMapper)getDao();
    }

    private final CarTypeRpcService carTypeRpcService;

    @Override
    public Integer deleteByCustomerAndMarket(Long customerId, Long marketId) {
        if (Objects.isNull(customerId)) {
            return 0;
        }
        VehicleInfo condition = new VehicleInfo();
        condition.setCustomerId(customerId);
        condition.setMarketId(marketId);
        return getActualMapper().delete(condition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchSaveOrUpdate(List<VehicleInfo> vehicleInfoList) {
        if (CollectionUtil.isEmpty(vehicleInfoList)) {
            return 0;
        }
        Set<Long> idSet = vehicleInfoList.stream().map(t -> t.getId()).filter(Objects::nonNull).collect(Collectors.toSet());
        VehicleInfoDto dto = new VehicleInfoDto();
        dto.setIdNotSet(idSet);
        dto.setCustomerId(vehicleInfoList.get(0).getCustomerId());
        dto.setMarketId(vehicleInfoList.get(0).getMarketId());
        //先删除数据库中已存在，但是不在本次传入的数据中的地址信息
        this.deleteByExample(dto);
        LocalDateTime now = LocalDateTime.now();
        vehicleInfoList.forEach(t -> {
            t.setModifyTime(now);
            if (Objects.isNull(t.getId())) {
                t.setCreateTime(now);
                this.insert(t);
            } else {
                this.update(t);
            }
        });
        return vehicleInfoList.size();
    }

    @Override
    public List<VehicleInfo> listByCustomerAndMarket(Set<Long> customerIdSet, Long marketId) {
        VehicleInfoDto condition = new VehicleInfoDto();
        condition.setCustomerIdSet(customerIdSet);
        condition.setMarketId(marketId);
        return listByExample(condition);
    }

    @Override
    public Optional<String> bindingVehicle(VehicleInfo vehicleInfo) {
        VehicleInfo condition = new VehicleInfo();
        condition.setMarketId(vehicleInfo.getMarketId());
        condition.setCustomerId(vehicleInfo.getCustomerId());
        condition.setRegistrationNumber(vehicleInfo.getRegistrationNumber());
        List<VehicleInfo> vehicleInfoList = list(condition);
        vehicleInfo.setModifyTime(LocalDateTime.now());
        if (Objects.isNull(vehicleInfo.getId())) {
            if (CollectionUtil.isNotEmpty(vehicleInfoList)) {
                return Optional.of("此车辆信息已被绑定,无需重复绑定");
            }
            vehicleInfo.setCreateTime(vehicleInfo.getModifyTime());
            vehicleInfo.setCreatorId(vehicleInfo.getModifierId());
        } else {
            long count = vehicleInfoList.stream().filter(t -> t.getRegistrationNumber().equalsIgnoreCase(vehicleInfo.getRegistrationNumber()) && !t.getId().equals(vehicleInfo.getId())).count();
            if (count > 0) {
                return Optional.of("此车辆信息已被绑定,无需重复绑定");
            }
        }
        this.saveOrUpdateSelective(vehicleInfo);
        return Optional.empty();
    }

    @Override
    public List<VehicleInfo> listByCustomerAndMarket(Long customerId, Long marketId) {
        VehicleInfo condition = new VehicleInfo();
        condition.setCustomerId(customerId);
        condition.setMarketId(marketId);
        List<VehicleInfo> list = this.list(condition);
        if (CollectionUtil.isNotEmpty(list)) {
            Map<Long, CarTypeDTO> longCarTypeDTOMap = carTypeRpcService.listMapInfo();
            list.forEach(t -> {
                if (longCarTypeDTOMap.containsKey(t.getTypeNumber())) {
                    t.setMetadata("carTypeName", longCarTypeDTOMap.get(t.getTypeNumber()).getName());
                }
            });
        }
        return list;
    }
}

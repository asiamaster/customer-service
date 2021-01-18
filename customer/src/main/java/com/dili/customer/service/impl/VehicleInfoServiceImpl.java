package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.customer.domain.VehicleInfo;
import com.dili.customer.domain.dto.VehicleInfoDto;
import com.dili.customer.mapper.VehicleInfoMapper;
import com.dili.customer.service.VehicleInfoService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/11/29 11:07
 */
@Service
public class VehicleInfoServiceImpl extends BaseServiceImpl<VehicleInfo, Long> implements VehicleInfoService {

    public VehicleInfoMapper getActualMapper() {
        return (VehicleInfoMapper)getDao();
    }

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
        vehicleInfoList.forEach(t -> {
            if (Objects.isNull(t.getId())) {
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
    public String bindingVehicle(VehicleInfo vehicleInfo) {
        VehicleInfo condition = new VehicleInfo();
        condition.setMarketId(vehicleInfo.getMarketId());
        condition.setCustomerId(vehicleInfo.getCustomerId());
        condition.setRegistrationNumber(vehicleInfo.getRegistrationNumber());
        if (CollectionUtil.isNotEmpty(list(condition))) {
            return "此车辆信息已被绑定,无需重复绑定";
        }
        vehicleInfo.setCreateTime(LocalDateTime.now());
        vehicleInfo.setModifyTime(vehicleInfo.getCreateTime());
        this.insert(vehicleInfo);
        return null;
    }
}

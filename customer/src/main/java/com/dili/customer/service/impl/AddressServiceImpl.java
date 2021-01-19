package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.domain.Address;
import com.dili.customer.domain.dto.AddressDto;
import com.dili.customer.mapper.AddressMapper;
import com.dili.customer.service.AddressService;
import com.dili.ss.base.BaseServiceImpl;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-01-02 16:19:35.
 */
@Service
public class AddressServiceImpl extends BaseServiceImpl<Address, Long> implements AddressService {

    public AddressMapper getActualMapper() {
        return (AddressMapper)getDao();
    }

    @Override
    public Integer deleteByCustomerAndMarket(Long customerId, Long marketId) {
        if (Objects.isNull(customerId)) {
            return 0;
        }
        Address address = new Address();
        address.setCustomerId(customerId);
        address.setMarketId(marketId);
        return getActualMapper().delete(address);
    }

    @Override
    public Integer batchSaveOrUpdate(List<Address> addressList) {
        if (CollectionUtil.isEmpty(addressList)) {
            return 0;
        }
        Set<Long> idSet = addressList.stream().map(t -> t.getId()).filter(Objects::nonNull).collect(Collectors.toSet());
        AddressDto dto = new AddressDto();
        dto.setIdNotSet(idSet);
        dto.setCustomerId(addressList.get(0).getCustomerId());
        dto.setMarketId(addressList.get(0).getMarketId());
        //先删除数据库中已存在，但是不在本次传入的数据中的地址信息
        this.deleteByExample(dto);
        addressList.forEach(t -> {
            if (Objects.isNull(t.getId())) {
                this.insert(t);
            } else {
                this.update(t);
            }
        });
        return addressList.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<String> saveAddress(Address address) {
        if (Objects.isNull(address.getModifyTime())) {
            address.setModifyTime(LocalDateTime.now());
        }
        if (Objects.isNull(address.getId())) {
            address.setCreateTime(address.getModifyTime());
            address.setCreatorId(address.getModifierId());
        }
        this.saveOrUpdate(address);
        if (YesOrNoEnum.YES.getCode().equals(address.getIsCurrent())) {
            updateDefaultFlag(address.getCustomerId(), address.getMarketId(), address.getId());
        }
        return Optional.empty();
    }

    @Override
    public void updateDefaultFlag(Long customerId, Long marketId, Long id) {
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
        params.put("customerId", customerId);
        params.put("marketId", marketId);
        params.put("id", id);
        getActualMapper().updateDefaultFlag(params);
    }
}
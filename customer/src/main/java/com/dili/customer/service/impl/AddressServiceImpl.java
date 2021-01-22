package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.commons.service.BusinessLogRpcService;
import com.dili.customer.domain.Address;
import com.dili.customer.domain.Customer;
import com.dili.customer.domain.dto.AddressDto;
import com.dili.customer.domain.dto.UapUserTicket;
import com.dili.customer.mapper.AddressMapper;
import com.dili.customer.service.AddressService;
import com.dili.customer.service.CustomerService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.mvc.util.RequestUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.util.WebContent;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-01-02 16:19:35.
 */
@RequiredArgsConstructor
@Service
public class AddressServiceImpl extends BaseServiceImpl<Address, Long> implements AddressService {

    public AddressMapper getActualMapper() {
        return (AddressMapper)getDao();
    }

    @Autowired
    private CustomerService customerService;
    private final BusinessLogRpcService businessLogRpcService;
    private final UapUserTicket uapUserTicket;

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
        LocalDateTime now = LocalDateTime.now();
        addressList.forEach(t -> {
            t.setModifyTime(now);
            if (Objects.isNull(t.getId())) {
                t.setCreateTime(t.getModifyTime());
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
        String operationType = "edit";
        UserTicket userTicket = uapUserTicket.getUserTicket();
        if (Objects.isNull(address.getModifierId())){
            address.setModifierId(userTicket.getId());
        }
        address.setModifyTime(LocalDateTime.now());
        if (Objects.isNull(address.getId())) {
            operationType = "add";
            address.setCreateTime(address.getModifyTime());
            address.setCreatorId(address.getModifierId());
        }
        this.saveOrUpdate(address);
        if (YesOrNoEnum.YES.getCode().equals(address.getIsCurrent())) {
            updateDefaultFlag(address.getCustomerId(), address.getMarketId(), address.getId());
        }
        Customer customer = customerService.get(address.getCustomerId());
        businessLogRpcService.asyncSave(customer.getId(), customer.getCode(), produceLoggerContent(address, "  修改后:"), "", operationType, userTicket, RequestUtils.getIpAddress(WebContent.getRequest()));
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

    @Override
    public Optional<String> deleteWithLogger(Long id) {
        Address address = this.get(id);
        if (Objects.isNull(address)) {
            return Optional.of("数据不存在");
        }
        Customer customer = customerService.get(address.getCustomerId());
        businessLogRpcService.asyncSave(customer.getId(), customer.getCode(), produceLoggerContent(address, ""), "操作渠道:APP", "del", uapUserTicket.getUserTicket(), RequestUtils.getIpAddress(WebContent.getRequest()));
        return Optional.empty();
    }

    /**
     * 构建日志存储对象
     * @param address 地址信息
     * @param prefix 内容前缀
     * @return
     */
    private String produceLoggerContent(Address address, String prefix) {
        StringBuffer str = new StringBuffer(prefix);
        str.append("地址信息:").append(address.getCityName()).append(address.getAddress());
        YesOrNoEnum yesOrNoEnum = YesOrNoEnum.getYesOrNoEnum(address.getIsCurrent());
        if (Objects.nonNull(yesOrNoEnum)) {
            str.append("是否当前:").append(yesOrNoEnum.getName());
        }
        return str.toString();
    }
}
package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.customer.domain.CustomerMarket;
import com.dili.customer.domain.dto.CustomerMarketDto;
import com.dili.customer.mapper.CustomerMarketMapper;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.service.CustomerMarketService;
import com.dili.customer.service.remote.MarketRpcService;
import com.dili.ss.base.BaseServiceImpl;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-12-31 10:19:41.
 * @author yuehongbo
 */
@RequiredArgsConstructor
@Service
public class CustomerMarketServiceImpl extends BaseServiceImpl<CustomerMarket, Long> implements CustomerMarketService {

    public CustomerMarketMapper getActualMapper() {
        return (CustomerMarketMapper)getDao();
    }

    private final MarketRpcService marketRpcService;

    @Override
    public CustomerMarket queryByMarketAndCustomerId(Long marketId, Long customerId) {
        CustomerMarket info = new CustomerMarket();
        info.setMarketId(marketId);
        info.setCustomerId(customerId);
        List<CustomerMarket> firmInfos = list(info);
        return firmInfos.stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeGrade(Long customerId, Long marketId, CustomerEnum.Grade nextGrade) {
        CustomerMarket customerMarket = this.queryByMarketAndCustomerId(marketId, customerId);
        if (Objects.nonNull(customerMarket)) {
            Integer gradeCode = customerMarket.getGrade();
            CustomerEnum.Grade grade = CustomerEnum.Grade.getInstance(gradeCode);
            if (Objects.isNull(grade) || grade.next().contains(nextGrade)) {
                customerMarket.setGrade(nextGrade.getCode());
                this.update(customerMarket);
            }
        }
    }

    @Override
    public Map<String,List<CustomerMarketDto>> selectByContactsPhone(String phone) {
        List<CustomerMarketDto> customerMarketDtoList = getActualMapper().selectByContactsPhone(phone);
        Map<String,List<CustomerMarketDto>> resultMap = Maps.newHashMap();
        if (CollectionUtil.isNotEmpty(customerMarketDtoList)){
            Map<String, String> marketMap = marketRpcService.listForMap();
            Map<Long, List<CustomerMarketDto>> collect = customerMarketDtoList.stream().collect(Collectors.groupingBy(CustomerMarketDto::getMarketId));
            collect.forEach((k, v) -> {
                resultMap.put(marketMap.get(String.valueOf(k)),v);
            });
        }
        return resultMap;
    }
}
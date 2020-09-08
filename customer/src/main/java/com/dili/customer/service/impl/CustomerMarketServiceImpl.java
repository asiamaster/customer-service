package com.dili.customer.service.impl;

import com.dili.customer.domain.CustomerMarket;
import com.dili.customer.mapper.CustomerMarketMapper;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.service.CustomerMarketService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-12-31 10:19:41.
 * @author yuehongbo
 */
@Service
public class CustomerMarketServiceImpl extends BaseServiceImpl<CustomerMarket, Long> implements CustomerMarketService {

    public CustomerMarketMapper getActualMapper() {
        return (CustomerMarketMapper)getDao();
    }

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
}
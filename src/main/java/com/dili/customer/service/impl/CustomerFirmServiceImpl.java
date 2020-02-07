package com.dili.customer.service.impl;

import com.dili.customer.mapper.CustomerFirmMapper;
import com.dili.customer.domain.CustomerFirm;
import com.dili.customer.service.CustomerFirmService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-12-31 10:19:41.
 * @author yuehongbo
 */
@Service
public class CustomerFirmServiceImpl extends BaseServiceImpl<CustomerFirm, Long> implements CustomerFirmService {

    public CustomerFirmMapper getActualMapper() {
        return (CustomerFirmMapper)getDao();
    }

    @Override
    public CustomerFirm queryByFirmAndCustomerId(Long marketId, Long customerId) {
        CustomerFirm info = new CustomerFirm();
        info.setMarketId(marketId);
        info.setCustomerId(customerId);
        List<CustomerFirm> firmInfos = list(info);
        return firmInfos.stream().findFirst().orElse(null);
    }
}
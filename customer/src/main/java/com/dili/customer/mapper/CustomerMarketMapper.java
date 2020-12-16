package com.dili.customer.mapper;

import com.dili.customer.domain.CustomerMarket;
import com.dili.ss.base.MyMapper;

import java.util.List;
import java.util.Map;

/**
 * @author yuehongbo
 */
public interface CustomerMarketMapper extends MyMapper<CustomerMarket> {

    /**
     *
     * @param marketId
     * @return
     */
    List<Map<String, Object>> statisticsApproval(Long marketId);
}
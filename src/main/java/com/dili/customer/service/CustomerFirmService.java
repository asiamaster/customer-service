package com.dili.customer.service;

import com.dili.customer.domain.CustomerFirm;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-12-31 10:19:41.
 * @author yuehongbo
 */
public interface CustomerFirmService extends BaseService<CustomerFirm, Long> {

    /**
     * 根据客户ID及所属市场，获取客户当前市场信息
     * @param marketId 市场ID
     * @param customerId 客户ID
     * @return
     */
    CustomerFirm queryByFirmAndCustomerId(Long marketId, Long customerId);
}
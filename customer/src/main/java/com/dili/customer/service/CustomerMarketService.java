package com.dili.customer.service;

import com.dili.customer.domain.CustomerMarket;
import com.dili.customer.domain.dto.CustomerMarketDto;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.ss.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-12-31 10:19:41.
 * @author yuehongbo
 */
public interface CustomerMarketService extends BaseService<CustomerMarket, Long> {

    /**
     * 根据客户ID及所属市场，获取客户当前市场信息
     * @param marketId 市场ID
     * @param customerId 客户ID
     * @return
     */
    CustomerMarket queryByMarketAndCustomerId(Long marketId, Long customerId);

    /**
     * 更改客户所在市场的客户等级
     * @param customerId 客户ID
     * @param marketId 所属市场ID
     * @param nextGrade 想要更新成的等级
     */
    void changeGrade(Long customerId, Long marketId, CustomerEnum.Grade nextGrade);

    /**
     * 根据客户ID查询客户市场归类信息
     * @param customerId 客户ID
     * @return
     */
    List<CustomerMarketDto> selectByCustomerId(Long customerId);
}
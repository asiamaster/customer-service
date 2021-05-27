package com.dili.customer.service;

import com.dili.customer.domain.CustomerMarket;
import com.dili.customer.domain.dto.CustomerMarketCharacterTypeDto;
import com.dili.customer.domain.dto.CustomerMarketDto;
import com.dili.customer.sdk.domain.dto.MarketApprovalResultInput;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.ss.base.BaseService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    Boolean changeGrade(Long customerId, Long marketId, CustomerEnum.Grade nextGrade);

    /**
     * 根据客户ID查询客户市场归类信息
     * @param customerId 客户ID
     * @return
     */
    List<CustomerMarketDto> selectByCustomerId(Long customerId);

    /**
     * 根据市场ID统计市场客户的审核情况
     * @param marketId 市场ID
     * @return
     */
    List<Map<String, Object>> statisticsApproval(Long marketId);

    /**
     * 客户市场信息审核结果
     * @param input
     */
    Optional<String> approval(MarketApprovalResultInput input);

    /**
     * 更改客户状态
     * @param customerId 客户ID
     * @param marketId 客户市场
     * @param state 客户状态
     */
    Optional<String> updateState(Long customerId, Long marketId, Integer state);

    /**
     * 获取市场和其他类型子角色分类信息的组合对象
     * @param marketIds 市场id集合
     * @return
     */
    List<CustomerMarketCharacterTypeDto> listOtherCharacterType(List<Long> marketIds);

}
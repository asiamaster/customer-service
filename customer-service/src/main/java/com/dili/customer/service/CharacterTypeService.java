package com.dili.customer.service;

import com.dili.customer.domain.CharacterType;
import com.dili.ss.base.BaseService;

import java.util.List;
import java.util.Set;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/11/26 11:28
 */
public interface CharacterTypeService extends BaseService<CharacterType, Long> {

    /**
     * 根据客户及市场，获取对应的客户角色身份信息
     * @param customerSets
     * @param marketId
     * @return
     */
    List<CharacterType> listByCustomerAndMarket(Set<Long> customerSets, Long marketId);

    /**
     * 批量保存客户身份信息
     * @param characterTypeList 客户身份信息
     * @param customerId       客户ID
     * @param marketId         市场ID
     * @return 返回保存后的客户身份信息
     */
    Integer saveInfo(List<CharacterType> characterTypeList, Long customerId, Long marketId);

    /**
     * 根据单个id及多个市场id，查询客户角色信息
     * @param customerId 客户ID
     * @param marketIds 市场ID集合
     * @return
     */
    List<CharacterType> listByCustomerAndMarkets(Long customerId, Set<Long> marketIds);
}

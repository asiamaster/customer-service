package com.dili.customer.service;

import com.dili.customer.domain.TallyingArea;
import com.dili.ss.base.BaseService;

import java.util.List;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/7/15 18:30
 */
public interface TallyingAreaService extends BaseService<TallyingArea, Long> {

    /**
     * 根据客户及市场，删除对应的(无租赁关系的)理货区信息
     * @param customerId 客户ID
     * @param marketId  市场ID
     * @return
     */
    Integer deleteByCustomerId(Long customerId,Long marketId);

    /**
     * 批量保存客户理货区信息
     * @param tallyingAreaList 理货区数据
     * @param customerId       客户ID
     * @param marketId         市场ID
     * @return
     */
    Integer saveInfo(List<TallyingArea> tallyingAreaList, Long customerId, Long marketId);
}

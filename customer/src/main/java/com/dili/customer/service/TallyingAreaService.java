package com.dili.customer.service;

import com.dili.customer.domain.TallyingArea;
import com.dili.ss.base.BaseService;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/7/15 18:30
 */
public interface TallyingAreaService extends BaseService<TallyingArea, Long> {

    /**
     * 根据客户及市场，删除对应的理货区信息
     * @param customerId 客户ID
     * @param marketId  市场ID
     * @return
     */
    Integer deleteByCustomerId(Long customerId,Long marketId);
}

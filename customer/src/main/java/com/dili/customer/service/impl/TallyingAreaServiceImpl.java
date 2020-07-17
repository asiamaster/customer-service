package com.dili.customer.service.impl;

import com.dili.customer.domain.TallyingArea;
import com.dili.customer.mapper.TallyingAreaMapper;
import com.dili.customer.service.TallyingAreaService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/7/15 18:31
 */
@Service
public class TallyingAreaServiceImpl extends BaseServiceImpl<TallyingArea, Long> implements TallyingAreaService {

    public TallyingAreaMapper getActualMapper() {
        return (TallyingAreaMapper)getDao();
    }

    @Override
    public Integer deleteByCustomerId(Long customerId, Long marketId) {
        if (Objects.isNull(customerId)) {
            return 0;
        }
        TallyingArea condition = new TallyingArea();
        condition.setCustomerId(customerId);
        condition.setMarketId(marketId);
        return deleteByExample(condition);
    }
}

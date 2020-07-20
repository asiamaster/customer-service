package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.customer.domain.TallyingArea;
import com.dili.customer.mapper.TallyingAreaMapper;
import com.dili.customer.service.TallyingAreaService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public Integer saveInfo(List<TallyingArea> tallyingAreaList) {
        //如果传入的客户理货区为空，则表示该客户在该市场没有租赁理货区(手动关联的，可编辑)，所有可以直接删除
        if (CollectionUtil.isEmpty(tallyingAreaList)) {
            return 0;
        }
        //根据客户及市场，查询出该客户在市场中的已有理货区信息
        Long customerId = tallyingAreaList.get(0).getCustomerId();
        Long marketId = tallyingAreaList.get(0).getMarketId();
        TallyingArea query = new TallyingArea();
        query.setCustomerId(customerId);
        query.setMarketId(marketId);
        List<TallyingArea> list = this.list(query);
        //如果该理货区信息在客户市场理货区中已存在，则忽略；如果不存在，则保存，且默认为无租赁关系
        if (CollectionUtil.isNotEmpty(list)) {
            //获取有租赁关系的资产ID
            Set<Long> leaseAssetsId = list.stream().filter(t -> 1 == t.getIsLease()).map(t -> t.getAssetsId()).collect(Collectors.toSet());
            //从传入的参数中移除有租赁关系的数据,参数中剩下全无租赁关系的数据
            tallyingAreaList = tallyingAreaList.stream().filter(t -> leaseAssetsId.contains(t.getAssetsId())).collect(Collectors.toList());
            //删除数据库中，无租赁关系的数据
            TallyingArea delCondition = new TallyingArea();
            delCondition.setCustomerId(customerId);
            delCondition.setMarketId(marketId);
            delCondition.setIsLease(0);
            this.deleteByExample(delCondition);
            //保存所有传入的无租赁关系的理货区
            this.batchInsert(tallyingAreaList);
        } else {
            this.batchInsert(tallyingAreaList);
        }
        return null;
    }
}
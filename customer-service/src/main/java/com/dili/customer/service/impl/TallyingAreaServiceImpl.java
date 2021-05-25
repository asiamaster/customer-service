package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.customer.domain.TallyingArea;
import com.dili.customer.domain.dto.TallyingAreaDto;
import com.dili.customer.mapper.TallyingAreaMapper;
import com.dili.customer.service.TallyingAreaService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
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
        condition.setIsLease(0);
        return deleteByExample(condition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveInfo(List<TallyingArea> tallyingAreaList, Long customerId, Long marketId) {
        //删除客户的理货区(只删除没有租赁关系的)
        deleteByCustomerId(customerId, marketId);
        //如果传入的客户理货区为空，则表示该客户在该市场没有租赁理货区(手动关联的，可编辑)，所有可以直接删除
        if (CollectionUtil.isEmpty(tallyingAreaList)) {
            return 0;
        }
        /**
         * 设置初始值信息
         */
        tallyingAreaList.forEach(t -> {
            t.setIsLease(0);
            t.setCustomerId(customerId);
            t.setMarketId(marketId);
            t.setCreateTime(LocalDateTime.now());
            t.setModifyTime(t.getCreateTime());
            t.setIsUsable(1);
        });
        //根据客户及市场，查询出该客户在市场中的已有理货区信息
        TallyingArea query = new TallyingArea();
        query.setCustomerId(customerId);
        query.setMarketId(marketId);
        List<TallyingArea> list = this.list(query);
        //如果该理货区信息在客户市场理货区中已存在，则忽略；如果不存在，则保存，且默认为无租赁关系
        if (CollectionUtil.isNotEmpty(list)) {
            //获取有租赁关系的资产ID
            Set<Long> leaseAssetsId = list.stream().filter(t -> 1 == t.getIsLease()).map(t -> t.getAssetsId()).collect(Collectors.toSet());
            //从传入的参数中移除有租赁关系的数据,参数中剩下全无租赁关系的数据
            tallyingAreaList = tallyingAreaList.stream().filter(t -> !leaseAssetsId.contains(t.getAssetsId())).collect(Collectors.toList());
        }
        //可能传入的全是有租赁关系的数据，所以，过滤后集合可能为空
        if (CollectionUtil.isNotEmpty(tallyingAreaList)) {
            this.batchInsert(tallyingAreaList);
        }
        return tallyingAreaList.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer syncAssetsLease(List<TallyingArea> list) {
        if (CollectionUtil.isEmpty(list)) {
            return 0;
        }
        list.forEach(t -> {
            Optional<TallyingArea> temp = this.getBy(t.getCustomerId(), t.getMarketId(), t.getAssetsId());
            TallyingArea tallyingArea = new TallyingArea();
            BeanUtils.copyProperties(t, tallyingArea);
            if (temp.isPresent()) {
                tallyingArea.setId(temp.get().getId());
                tallyingArea.setCreateTime(temp.get().getCreateTime());
            } else {
                tallyingArea.setCreateTime(LocalDateTime.now());
            }
            tallyingArea.setModifyTime(LocalDateTime.now());
            if (5 == t.getState()) {
                tallyingArea.setIsUsable(1);
            } else {
                tallyingArea.setIsUsable(0);
            }
            tallyingArea.setIsLease(1);
            saveOrUpdate(tallyingArea);
        });
        return list.size();
    }

    @Override
    public Map<Long, List<TallyingArea>> batchQuery(Set<Long> customerIds, Long marketId) {
        TallyingAreaDto condition = new TallyingAreaDto();
        condition.setCustomerIdSet(customerIds);
        condition.setMarketId(marketId);
        List<TallyingArea> tallyingAreas = listByExample(condition);
        Map<Long, List<TallyingArea>> tallyingAreaMap = new HashMap<>();
        for (Long id : customerIds) {
            List<TallyingArea> categories = tallyingAreas.stream().filter(b -> id.equals(b.getCustomerId()))
                    .collect(Collectors.toList());
            tallyingAreaMap.put(id, categories);
            tallyingAreas.removeAll(categories);
        }
        return tallyingAreaMap;


    }


    /**
     * 根据客户、市场、资产获取对应的货位新
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @param assetId 资产ID
     * @return
     */
    private Optional<TallyingArea> getBy(Long customerId, Long marketId, Long assetId){
        TallyingArea condition = new TallyingArea();
        condition.setCustomerId(customerId);
        condition.setAssetsId(assetId);
        condition.setMarketId(marketId);
        List<TallyingArea> tallyingAreaList = list(condition);
        if (CollectionUtil.isEmpty(tallyingAreaList)) {
            return Optional.empty();
        }
        return Optional.ofNullable(tallyingAreaList.get(0));
    }
}

package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.dili.customer.domain.BusinessCategory;
import com.dili.customer.domain.dto.BusinessCategoryDto;
import com.dili.customer.mapper.BusinessCategoryMapper;
import com.dili.customer.service.BusinessCategoryService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 客户经营品类
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/8/31 14:49
 */
@Service
public class BusinessCategoryServiceImpl extends BaseServiceImpl<BusinessCategory,Long> implements BusinessCategoryService {

    public BusinessCategoryMapper getActualMapper() {
        return (BusinessCategoryMapper)getDao();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveInfo(List<BusinessCategory> businessCategoryList, Long customerId, Long marketId) {
        if (Objects.isNull(customerId) && Objects.isNull(marketId)) {
            return -1;
        }
        BusinessCategory deleteCondition = new BusinessCategory();
        deleteCondition.setCustomerId(customerId);
        deleteCondition.setMarketId(marketId);
        getActualMapper().delete(deleteCondition);
        if (CollectionUtil.isNotEmpty(businessCategoryList)) {
            businessCategoryList.forEach(t -> {
                LocalDateTime now = LocalDateTime.now();
                t.setId(IdUtil.getSnowflake(1, 1).nextId());
                t.setMarketId(marketId);
                t.setCustomerId(customerId);
                t.setModifyTime(now);
                t.setCreateTime(now);
            });
            this.batchInsert(businessCategoryList);
        }
        return 0;
    }

    @Override
    public List<BusinessCategory> listByCustomerAndMarket(Set<Long> customerIdSet, Long marketId) {
        BusinessCategoryDto condition = new BusinessCategoryDto();
        condition.setCustomerIdSet(customerIdSet);
        condition.setMarketId(marketId);
        return listByExample(condition);
    }

    @Override
    public Map<Long, List<BusinessCategory>> batchQuery(Set<Long> customerIds, Long marketId) {
        BusinessCategoryDto condition = new BusinessCategoryDto();
        condition.setCustomerIdSet(customerIds);
        condition.setMarketId(marketId);
        List<BusinessCategory> businessCategories = listByExample(condition);
        Map<Long, List<BusinessCategory>> businessCategoryMap = new HashMap<>();
        for (Long id : customerIds) {
            List<BusinessCategory> categories = businessCategories.stream().filter(b -> id.equals(b.getCustomerId()))
                    .collect(Collectors.toList());
            businessCategoryMap.put(id, categories);
            businessCategories.removeAll(categories);
        }
        return businessCategoryMap;
    }
}

package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.dili.customer.domain.CharacterType;
import com.dili.customer.domain.query.CharacterTypeQuery;
import com.dili.customer.mapper.CharacterTypeMapper;
import com.dili.customer.service.CharacterTypeService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/11/26 13:49
 */
@Service
public class CharacterTypeServiceImpl extends BaseServiceImpl<CharacterType, Long> implements CharacterTypeService {

    public CharacterTypeMapper getActualMapper() {
        return (CharacterTypeMapper)getDao();
    }

    @Override
    public List<CharacterType> listByCustomerAndMarket(Set<Long> customerSets, Long marketId) {
        if (CollectionUtil.isNotEmpty(customerSets) && Objects.nonNull(marketId)) {
            CharacterTypeQuery characterType = new CharacterTypeQuery();
            characterType.setCustomerIdSet(customerSets);
            characterType.setMarketId(marketId);
            return this.listByExample(characterType);
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveInfo(List<CharacterType> characterTypeList, Long customerId, Long marketId) {
        if (Objects.isNull(customerId) && Objects.isNull(marketId)) {
            return -1;
        }
        CharacterType deleteCondition = new CharacterType();
        deleteCondition.setCustomerId(customerId);
        deleteCondition.setMarketId(marketId);
        getActualMapper().delete(deleteCondition);
        if (CollectionUtil.isNotEmpty(characterTypeList)) {
            characterTypeList.forEach(t -> {
                LocalDateTime now = LocalDateTime.now();
                t.setId(IdUtil.getSnowflake(1, 1).nextId());
                t.setMarketId(marketId);
                t.setCustomerId(customerId);
                t.setModifyTime(now);
                t.setCreateTime(now);
            });
            this.batchInsert(characterTypeList);
        }
        return 0;
    }

    @Override
    public List<CharacterType> listByCustomerAndMarkets(Long customerId, Set<Long> marketIds) {
        if (Objects.nonNull(customerId) && Objects.nonNull(marketIds)) {
            CharacterTypeQuery characterType = new CharacterTypeQuery();
            Set<Long> customerSets = new HashSet<>();
            customerSets.add(customerId);
            characterType.setCustomerIdSet(customerSets);
            characterType.setMarketIdSet(marketIds);
            return this.listByExample(characterType);
        }
        return Collections.emptyList();


    }
}

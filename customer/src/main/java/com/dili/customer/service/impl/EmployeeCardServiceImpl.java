package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.domain.EmployeeCard;
import com.dili.customer.service.EmployeeCardService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/2 17:03
 */
@Service
public class EmployeeCardServiceImpl extends BaseServiceImpl<EmployeeCard, Long> implements EmployeeCardService {

    @Override
    public Optional<EmployeeCard> getByCustomerEmployeeIdAndCardAccountId(Long customerEmployeeId, Long cardAccountId, Long marketId) {
        EmployeeCard employeeCard = new EmployeeCard();
        employeeCard.setCustomerEmployeeId(customerEmployeeId);
        employeeCard.setCardAccountId(cardAccountId);
        employeeCard.setMarketId(marketId);
        employeeCard.setDeleted(YesOrNoEnum.NO.getCode());
        List<EmployeeCard> list = list(employeeCard);
        return CollectionUtil.isEmpty(list) ? Optional.empty() : Optional.ofNullable(list.get(0));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(Long customerEmployeeId, Long cardAccountId, Long marketId, String cardNo) {
        EmployeeCard employeeCardSaveData = new EmployeeCard();
        employeeCardSaveData.setCustomerEmployeeId(customerEmployeeId);
        employeeCardSaveData.setCardAccountId(cardAccountId);
        employeeCardSaveData.setMarketId(marketId);
        employeeCardSaveData.setCardNo(cardNo);
        employeeCardSaveData.setDeleted(YesOrNoEnum.NO.getCode());
        employeeCardSaveData.setCreateTime(LocalDateTime.now());
        employeeCardSaveData.setModifyTime(LocalDateTime.now());
        return this.insertSelective(employeeCardSaveData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int logicDelete(Long id) {
        EmployeeCard employeeCard = new EmployeeCard();
        employeeCard.setId(id);
        employeeCard.setDeleted(YesOrNoEnum.YES.getCode());
        employeeCard.setModifyTime(LocalDateTime.now());
        return this.updateSelective(employeeCard);
    }

    @Override
    public List<EmployeeCard> listByCustomerEmployeeId(Long customerEmployeeId) {
        EmployeeCard employeeCard = new EmployeeCard();
        employeeCard.setCustomerEmployeeId(customerEmployeeId);
        employeeCard.setDeleted(YesOrNoEnum.NO.getCode());
        return list(employeeCard);
    }

    @Override
    public List<EmployeeCard> listByCardNo(String cardNo) {
        EmployeeCard employeeCard = new EmployeeCard();
        employeeCard.setCardNo(cardNo);
        employeeCard.setDeleted(YesOrNoEnum.NO.getCode());
        return list(employeeCard);
    }
}

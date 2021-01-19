package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.domain.Contacts;
import com.dili.customer.domain.dto.ContactsDto;
import com.dili.customer.mapper.ContactsMapper;
import com.dili.customer.service.ContactsService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-01-02 16:18:39.
 */
@Service
public class ContactsServiceImpl extends BaseServiceImpl<Contacts, Long> implements ContactsService {

    public ContactsMapper getActualMapper() {
        return (ContactsMapper)getDao();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput saveContacts(Contacts customerContacts) {
        //构造查询条件，用于查询该客户是否已有该联系人
        Contacts condition = new Contacts();
        condition.setCustomerId(customerContacts.getCustomerId());
        condition.setMarketId(customerContacts.getMarketId());
        condition.setPhone(customerContacts.getPhone());
        List<Contacts> contactsList = list(condition);
        if (null == customerContacts.getId()) {
            if (CollectionUtil.isNotEmpty(contactsList)) {
                return BaseOutput.failure("该手机号对应的联系人已存在");
            }
            customerContacts.setCreatorId(customerContacts.getModifierId());
            customerContacts.setCreateTime(LocalDateTime.now());
            customerContacts.setModifyTime(customerContacts.getCreateTime());
            this.insertSelective(customerContacts);
            return BaseOutput.success();
        } else {
            Contacts temp = this.get(customerContacts.getId());
            if (temp == null){
                return BaseOutput.failure("数据已不存在，不能修改");
            }
            if (CollectionUtil.isNotEmpty(contactsList)) {
                Boolean exist = contactsList.stream().allMatch(c -> !Objects.equals(c.getId(), customerContacts.getId()) && Objects.equals(c.getPhone(), customerContacts.getPhone()) && Objects.equals(c.getMarketId(), customerContacts.getMarketId()));
                if (exist) {
                    return BaseOutput.failure("该手机号对应的联系人已存在");
                }
            }
            this.update(customerContacts);
        }
        if (YesOrNoEnum.YES.getCode().equals(customerContacts.getIsDefault())) {
            updateDefaultFlag(customerContacts.getCustomerId(), customerContacts.getMarketId(), customerContacts.getId());
        }
        return BaseOutput.success();
    }

    @Override
    public Integer deleteByCustomerId(Long customerId, Long marketId) {
        if (Objects.isNull(customerId)) {
            return 0;
        }
        Contacts condition = new Contacts();
        condition.setCustomerId(customerId);
        condition.setMarketId(marketId);
        return deleteByExample(condition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchSaveOrUpdate(List<Contacts> contactsList) {
        if (CollectionUtil.isEmpty(contactsList)) {
            return 0;
        }
        Set<Long> idSet = contactsList.stream().map(t -> t.getId()).filter(Objects::nonNull).collect(Collectors.toSet());
        ContactsDto dto = new ContactsDto();
        dto.setIdNotSet(idSet);
        dto.setCustomerId(contactsList.get(0).getCustomerId());
        dto.setMarketId(contactsList.get(0).getMarketId());
        this.deleteByExample(dto);
        contactsList.forEach(t -> {
            if (Objects.isNull(t.getId())) {
                this.insert(t);
            } else {
                this.update(t);
            }
        });
        return contactsList.size();
    }

    @Override
    public void updateDefaultFlag(Long customerId, Long marketId, Long id) {
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
        params.put("customerId", customerId);
        params.put("marketId", marketId);
        params.put("id", id);
        getActualMapper().updateDefaultFlag(params);
    }
}
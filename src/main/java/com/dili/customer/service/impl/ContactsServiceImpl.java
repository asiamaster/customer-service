package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.customer.domain.Contacts;
import com.dili.customer.mapper.ContactsMapper;
import com.dili.customer.service.ContactsService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    public BaseOutput saveContacts(Contacts customerContacts) {
        //构造查询条件，用于查询该客户是否已有该联系人
        Contacts condition = new Contacts();
        condition.setCustomerId(customerContacts.getCustomerId());
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
                Boolean exist = contactsList.stream().allMatch(c-> !Objects.equals(c.getId(),customerContacts.getId()) && Objects.equals(c.getPhone(),customerContacts.getPhone()));
                if (exist) {
                    return BaseOutput.failure("该手机号对应的联系人已存在");
                }
            }
            this.update(customerContacts);
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
}
package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.commons.service.BusinessLogRpcService;
import com.dili.customer.domain.Contacts;
import com.dili.customer.domain.Customer;
import com.dili.customer.domain.dto.ContactsDto;
import com.dili.customer.domain.dto.UapUserTicket;
import com.dili.customer.mapper.ContactsMapper;
import com.dili.customer.service.ContactsService;
import com.dili.customer.service.CustomerQueryService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.mvc.util.RequestUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.dili.uap.sdk.util.WebContent;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-01-02 16:18:39.
 */
@Service
public class ContactsServiceImpl extends BaseServiceImpl<Contacts, Long> implements ContactsService {

    public ContactsMapper getActualMapper() {
        return (ContactsMapper) getDao();
    }

    @Autowired
    private CustomerQueryService customerQueryService;
    @Autowired
    private BusinessLogRpcService businessLogRpcService;
    @Autowired
    private UapUserTicket uapUserTicket;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput saveContacts(Contacts customerContacts) {
        StringBuilder content = new StringBuilder();
        String operationType = "add";
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (Objects.isNull(customerContacts.getModifierId())) {
            customerContacts.setModifierId(userTicket.getId());
        }
        customerContacts.setModifyTime(LocalDateTime.now());
        //构造查询条件，用于查询该客户是否已有该联系人
        Contacts condition = new Contacts();
        condition.setCustomerId(customerContacts.getCustomerId());
        condition.setMarketId(customerContacts.getMarketId());
        condition.setPhone(customerContacts.getPhone());
        List<Contacts> contactsList = list(condition);
        if (Objects.isNull(customerContacts.getId())) {
            if (CollectionUtil.isNotEmpty(contactsList)) {
                return BaseOutput.failure("该手机号对应的联系人已存在");
            }
            customerContacts.setCreatorId(customerContacts.getModifierId());
            customerContacts.setCreateTime(LocalDateTime.now());
            customerContacts.setModifyTime(customerContacts.getCreateTime());
            this.insertSelective(customerContacts);
            content.append(produceLoggerContent(customerContacts, String.format("联系人数据新增 %s ： ", customerContacts.getId())));
        } else {
            Boolean exist = contactsList.stream().anyMatch(c -> !Objects.equals(c.getId(), customerContacts.getId()) && Objects.equals(c.getPhone(), customerContacts.getPhone()) && Objects.equals(c.getMarketId(), customerContacts.getMarketId()));
            if (exist) {
                return BaseOutput.failure("该手机号对应的联系人已存在");
            }
            Contacts contacts = this.get(customerContacts.getId());
            if (Objects.isNull(contacts)) {
                return BaseOutput.failure("数据已不存在，不能修改");
            }
            content.append(produceLoggerContent(contacts, String.format("联系人数据 %s 修改前：", customerContacts.getId())));
            this.update(customerContacts);
            content.append(produceLoggerContent(customerContacts, "<br/>修改后："));
            operationType = "edit";
        }
        if (YesOrNoEnum.YES.getCode().equals(customerContacts.getIsDefault())) {
            updateDefaultFlag(customerContacts.getCustomerId(), customerContacts.getMarketId(), customerContacts.getId());
        }
        Customer customer = customerQueryService.get(customerContacts.getCustomerId());
        businessLogRpcService.asyncSave(customer.getId(), customer.getCode(), content.toString(), "操作渠道:APP", operationType, userTicket, RequestUtils.getIpAddress(WebContent.getRequest()));
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
        LocalDateTime now = LocalDateTime.now();
        contactsList.forEach(t -> {
            t.setModifyTime(now);
            if (Objects.isNull(t.getId())) {
                t.setCreateTime(t.getModifyTime());
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

    @Override
    public Optional<String> deleteWithLogger(Long id) {
        Contacts contacts = this.get(id);
        if (Objects.isNull(contacts)) {
            return Optional.of("数据不存在");
        }
        this.delete(id);
        Customer customer = customerQueryService.get(contacts.getCustomerId());
        businessLogRpcService.asyncSave(customer.getId(), customer.getCode(), produceLoggerContent(contacts, String.format("联系人数据ID %s :", id)), "操作渠道:APP", "del", SessionContext.getSessionContext().getUserTicket(), RequestUtils.getIpAddress(WebContent.getRequest()));
        return Optional.empty();
    }


    /**
     * 构建日志存储对象
     * @param contacts 联系人信息
     * @param prefix   内容前缀
     * @return
     */
    private String produceLoggerContent(Contacts contacts, String prefix) {
        StringBuffer str = new StringBuffer(prefix);
        str.append(" 联系人：").append(Objects.toString(contacts.getName(), "- "))
                .append(" 职务：").append(Objects.toString(contacts.getPosition(), "- "))
                .append(" 电话：").append(Objects.toString(contacts.getPhone(), "- "))
                .append(" 地址：").append(Objects.toString(contacts.getAddress(), "- "));
        YesOrNoEnum yesOrNoEnum = YesOrNoEnum.getYesOrNoEnum(contacts.getIsDefault());
        if (Objects.nonNull(yesOrNoEnum)) {
            str.append("是否默认:").append(yesOrNoEnum.getName());
        }
        return str.toString();
    }
}
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
import com.dili.customer.service.CustomerService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.mvc.util.RequestUtils;
import com.dili.uap.sdk.util.WebContent;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Service
public class ContactsServiceImpl extends BaseServiceImpl<Contacts, Long> implements ContactsService {

    public ContactsMapper getActualMapper() {
        return (ContactsMapper)getDao();
    }
    @Autowired
    private CustomerService customerService;
    private final BusinessLogRpcService businessLogRpcService;
    private final UapUserTicket uapUserTicket;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput saveContacts(Contacts customerContacts) {
        StringBuilder content = new StringBuilder();
        String operationType = "add";
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
            content.append(produceLoggerContent(customerContacts, ""));
            this.insertSelective(customerContacts);
            return BaseOutput.success();
        } else {
            Contacts temp = this.get(customerContacts.getId());
            if (temp == null) {
                return BaseOutput.failure("数据已不存在，不能修改");
            }
            content.append(produceLoggerContent(temp, "修改前:"));
            if (CollectionUtil.isNotEmpty(contactsList)) {
                Boolean exist = contactsList.stream().allMatch(c -> !Objects.equals(c.getId(), customerContacts.getId()) && Objects.equals(c.getPhone(), customerContacts.getPhone()) && Objects.equals(c.getMarketId(), customerContacts.getMarketId()));
                if (exist) {
                    return BaseOutput.failure("该手机号对应的联系人已存在");
                }
            }
            this.update(customerContacts);
            content.append(produceLoggerContent(customerContacts, "  修改后:"));
            operationType = "edit";
        }
        if (YesOrNoEnum.YES.getCode().equals(customerContacts.getIsDefault())) {
            updateDefaultFlag(customerContacts.getCustomerId(), customerContacts.getMarketId(), customerContacts.getId());
        }
        Customer customer = customerService.get(customerContacts.getCustomerId());
        businessLogRpcService.asyncSave(customer.getId(), customer.getCode(), content.toString(), "", operationType, uapUserTicket.getUserTicket(), RequestUtils.getIpAddress(WebContent.getRequest()));
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

    @Override
    public Optional<String> deleteWithLogger(Long id) {
        Contacts contacts = this.get(id);
        if (Objects.isNull(contacts)) {
            return Optional.of("数据不存在");
        }
        Customer customer = customerService.get(contacts.getCustomerId());
        businessLogRpcService.asyncSave(customer.getId(), customer.getCode(), produceLoggerContent(contacts, ""), "操作渠道:APP", "del", uapUserTicket.getUserTicket(), RequestUtils.getIpAddress(WebContent.getRequest()));
        return Optional.empty();
    }


    /**
     * 构建日志存储对象
     * @param contacts 联系人信息
     * @param prefix 内容前缀
     * @return
     */
    private String produceLoggerContent(Contacts contacts, String prefix) {
        StringBuffer str = new StringBuffer(prefix);
        str.append("联系人:").append(contacts.getName())
                .append("职务:").append(contacts.getPosition())
                .append("电话:").append(contacts.getPhone())
                .append("地址:").append(contacts.getAddress());
        YesOrNoEnum yesOrNoEnum = YesOrNoEnum.getYesOrNoEnum(contacts.getIsDefault());
        if (Objects.nonNull(yesOrNoEnum)) {
            str.append("是否默认:").append(yesOrNoEnum.getName());
        }
        return str.toString();
    }
}
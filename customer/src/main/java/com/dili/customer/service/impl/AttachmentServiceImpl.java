package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.customer.commons.service.BusinessLogRpcService;
import com.dili.customer.domain.Attachment;
import com.dili.customer.domain.Customer;
import com.dili.customer.domain.dto.AttachmentDto;
import com.dili.customer.domain.dto.UapUserTicket;
import com.dili.customer.mapper.AttachmentMapper;
import com.dili.customer.sdk.domain.dto.AttachmentGroupInfo;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.service.AttachmentService;
import com.dili.customer.service.CustomerManageService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.mvc.util.RequestUtils;
import com.dili.uap.sdk.util.WebContent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 客户附件信息
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-25 17:14:18.
 */
@RequiredArgsConstructor
@Service
public class AttachmentServiceImpl extends BaseServiceImpl<Attachment, Long> implements AttachmentService {

    public AttachmentMapper getActualMapper() {
        return (AttachmentMapper)getDao();
    }

    @Autowired
    private CustomerManageService customerManageService;
    private final BusinessLogRpcService businessLogRpcService;
    private final UapUserTicket uapUserTicket;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchSave(List<Attachment> attachmentList) {
        if (CollectionUtil.isEmpty(attachmentList)) {
            return 0;
        }
        return batchSave(attachmentList,attachmentList.get(0).getCustomerId(),attachmentList.get(0).getMarketId());
    }

    @Override
    public Integer batchSave(List<Attachment> attachmentList, Long customerId, Long marketId) {
        if (CollectionUtil.isEmpty(attachmentList)) {
            return 0;
        }
        Set<Long> idSet = attachmentList.stream().map(t -> t.getId()).filter(Objects::nonNull).collect(Collectors.toSet());
        AttachmentDto dto = new AttachmentDto();
        dto.setIdNotSet(idSet);
        dto.setCustomerId(customerId);
        dto.setMarketId(marketId);
        //先删除数据库中已存在，但是不在本次传入的数据中的图片信息
        this.deleteByExample(dto);
        attachmentList.forEach(t -> {
            t.setCustomerId(customerId);
            t.setMarketId(marketId);
            t.setModifyTime(LocalDateTime.now());
            if (Objects.isNull(t.getId())) {
                t.setCreateTime(t.getModifyTime());
                this.insert(t);
            } else {
                this.update(t);
            }
        });
        return attachmentList.size();
    }

    @Override
    public List<AttachmentGroupInfo> listAttachment(Long customerId, Long marketId, String organizationType) {
        if (Objects.nonNull(customerId) && Objects.nonNull(marketId)) {
            Attachment condition = new Attachment();
            condition.setCustomerId(customerId);
            condition.setMarketId(marketId);
            List<Attachment> attachmentList = list(condition);
            return convertToGroup(attachmentList, organizationType);
        }
        return Collections.emptyList();
    }

    @Override
    public List<AttachmentGroupInfo> convertToGroup(List<Attachment> attachmentList, String organizationType) {
        if (CollectionUtil.isNotEmpty(attachmentList)) {
            Map<Integer, List<Attachment>> integerListMap = Maps.newHashMap();
            integerListMap.putAll(StreamEx.of(attachmentList).filter(Objects::nonNull).groupingBy(Attachment::getFileType));
            //根据客户类型，获取该类型的客户能操作的附件类型
            List<CustomerEnum.AttachmentType> attachmentTypeList = CustomerEnum.AttachmentType.getByOrganizationType(CustomerEnum.OrganizationType.getInstance(organizationType));
            List<AttachmentGroupInfo> resultData = Lists.newArrayList();
            attachmentTypeList.forEach(t -> {
                AttachmentGroupInfo info = new AttachmentGroupInfo();
                info.setCode(t.getCode());
                info.setValue(t.getValue());
                if (integerListMap.containsKey(t.getCode())) {
                    info.setAttachmentList(JSONArray.parseArray(JSONObject.toJSONString(integerListMap.get(t.getCode())), com.dili.customer.sdk.domain.Attachment.class));
                }
                resultData.add(info);
            });
            return resultData;
        }
        return Collections.emptyList();
    }

    @Override
    public List<Attachment> listByCustomerAndMarket(Set<Long> customerIdSet, Long marketId) {
        AttachmentDto condition = new AttachmentDto();
        condition.setCustomerIdSet(customerIdSet);
        condition.setMarketId(marketId);
        return listByExample(condition);
    }

    @Override
    public void deleteByCustomerAndMarket(Long customerId, Long marketId) {
        if (Objects.nonNull(customerId) && Objects.nonNull(marketId)) {
            Attachment condition = new Attachment();
            condition.setCustomerId(customerId);
            condition.setMarketId(marketId);
            getActualMapper().delete(condition);
            Customer customer = customerManageService.get(customerId);
            businessLogRpcService.asyncSave(customerId, customer.getCode(), "删除所有附件", "", "del", uapUserTicket.getUserTicket(), RequestUtils.getIpAddress(WebContent.getRequest()));
        }
    }

}
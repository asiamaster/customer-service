package com.dili.customer.service;

import com.dili.customer.domain.Attachment;
import com.dili.customer.sdk.domain.dto.AttachmentGroupInfo;
import com.dili.ss.base.BaseService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 客户附件信息
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-25 17:14:18.
 */
public interface AttachmentService extends BaseService<Attachment, Long> {

    /**
     * 批量保存数据
     * @param attachmentList
     * @return
     */
    Integer batchSave(List<Attachment> attachmentList);

    /**
     * 批量保存数据
     * @param attachmentList 附件信息
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @return
     */
    Integer batchSave(List<Attachment> attachmentList, Long customerId, Long marketId);

    /**
     * 根据客户ID查询该客户的附件信息
     * @param customerId 客户ID
     * @param marketId 所属市场
     * @param organizationType 客户组织类型
     * @return
     */
    List<AttachmentGroupInfo> listAttachment(Long customerId, Long marketId, String organizationType);

    /**
     * 根据客户类型组装分组数据
     * @param attachmentList 附件信息
     * @param organizationType 客户组织类型
     * @return
     */
    List<AttachmentGroupInfo> convertToGroup(List<Attachment> attachmentList,String organizationType);

    /**
     * 根据客户及市场获取附件信息
     * @param customerIdSet 客户ID集合
     * @param marketId 市场ID
     * @return
     */
    List<Attachment> listByCustomerAndMarket(Set<Long> customerIdSet, Long marketId);

    /**
     * 根据客户及市场，删除客户的附件信息
     * @param customerId 客户ID
     * @param marketId 市场ID
     */
    void deleteByCustomerAndMarket(Long customerId, Long marketId);

    /**
     * 根据批量客户ID查询客户附件信息
     * @param customerIds 客户ID
     * @param marketId 所属市场
     * @return
     */
    Map<Long, List<Attachment>> batchQuery(Set<Long> customerIds, Long marketId);
}
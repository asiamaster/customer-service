package com.dili.customer.service;

import com.dili.customer.domain.Attachment;
import com.dili.customer.sdk.domain.dto.AttachmentGroupInfo;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
    @PostMapping(value = "/listAttachment")
    List<AttachmentGroupInfo> listAttachment(Long customerId, Long marketId, String organizationType);
}
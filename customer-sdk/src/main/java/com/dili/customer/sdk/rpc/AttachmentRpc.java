package com.dili.customer.sdk.rpc;

import com.dili.customer.sdk.domain.Attachment;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * 客户附件信息服务
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/7/25 17:43
 */
@FeignClient(name = "customer-service", contextId = "attachmentRpc", url = "${customerService.url:}")
public interface AttachmentRpc {

    /**
     * 根据客户ID查询该客户的附件信息
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @return 客户附件信息
     */
    @PostMapping(value = "/api/attachment/listAttachment")
    BaseOutput<List<Attachment>> listAttachment(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId);

    /**
     * 根据条件查询该客户的附件信息
     * @param attachment 查询条件
     * @return 客户附件信息
     */
    @PostMapping(value = "/api/attachment/listByExample")
    BaseOutput<List<Attachment>> listByExample(Attachment attachment);

    /**
     * 根据客户信息删除该客户的附件信息
     * @param customerId 客户ID
     * @param marketId   市场ID
     * @return 是否删除成功
     */
    @PostMapping(value = "/api/attachment/delete")
    BaseOutput<Boolean> delete(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId, @RequestParam(name = "idSet", required = false) Set<Long> idSet);

    /**
     * 批量保存客户附件信息
     * @param attachmentList 客户附件信息
     * @return 保存结果
     */
    @PostMapping(value = "/api/attachment/batchSave")
    BaseOutput batchSave(List<Attachment> attachmentList);
}

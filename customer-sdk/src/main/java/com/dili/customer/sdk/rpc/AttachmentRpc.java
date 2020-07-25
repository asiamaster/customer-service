package com.dili.customer.sdk.rpc;

import com.dili.customer.sdk.domain.Attachment;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
     * @return
     */
    @RequestMapping(value = "/api/attachment/listAttachment", method = RequestMethod.POST)
    BaseOutput<List<Attachment>> listTallyingArea(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId);

    /**
     * 根据条件查询该客户的附件信息
     * @param attachment 查询条件
     * @return
     */
    @RequestMapping(value = "/api/attachment/listByExample", method = RequestMethod.POST)
    BaseOutput<List<Attachment>> listByExample(Attachment attachment);
}

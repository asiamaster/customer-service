package com.dili.customer.api;

import com.dili.customer.domain.Attachment;
import com.dili.customer.service.AttachmentService;
import com.dili.ss.domain.BaseOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户附件信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/7/25 17:20
 */
@RestController
@RequestMapping("/api/attachment")
@Slf4j
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    /**
     * 根据客户ID查询该客户的附件信息
     * @param customerId 客户ID
     * @param marketId 所属市场
     * @return
     */
    @RequestMapping(value = "/listAttachment", method = {RequestMethod.POST})
    public BaseOutput<List<Attachment>> listAttachment(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId) {
        Attachment condition = new Attachment();
        condition.setCustomerId(customerId);
        condition.setMarketId(marketId);
        return BaseOutput.success().setData(attachmentService.list(condition));
    }

    /**
     * 根据条件查询该客户的附件信息
     * @param attachment 查询条件
     * @return
     */
    @RequestMapping(value = "/listByExample", method = {RequestMethod.POST})
    public BaseOutput<List<Attachment>> listByExample(@RequestBody Attachment attachment) {
        return BaseOutput.success().setData(attachmentService.listByExample(attachment));
    }
}

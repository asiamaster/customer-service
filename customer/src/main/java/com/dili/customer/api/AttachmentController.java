package com.dili.customer.api;

import com.dili.customer.annotation.UapToken;
import com.dili.customer.domain.Attachment;
import com.dili.customer.domain.dto.AttachmentDto;
import com.dili.customer.service.AttachmentService;
import com.dili.ss.domain.BaseOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
    @PostMapping(value = "/listAttachment")
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
    @PostMapping(value = "/listByExample")
    public BaseOutput<List<Attachment>> listByExample(@RequestBody AttachmentDto attachment) {
        return BaseOutput.success().setData(attachmentService.listByExample(attachment));
    }

    /**
     * 根据条件删除客户的附件信息
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @return 删除结果
     */
    @UapToken
    @PostMapping(value = "/delete")
    public BaseOutput delete(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId) {
        if (Objects.nonNull(customerId) && Objects.nonNull(marketId)) {
            attachmentService.deleteByCustomerAndMarket(customerId, marketId);
            return BaseOutput.success().setData(true);
        }
        return BaseOutput.failure("必要参数丢失").setData(false);
    }

    /**
     * 批量保存客户附件信息
     * @param attachmentList 附件信息
     * @return
     */
    @PostMapping(value = "/batchSave")
    public BaseOutput batchSave(@RequestBody List<Attachment> attachmentList) {
        return BaseOutput.success().setData(attachmentService.batchSave(attachmentList));
    }

}

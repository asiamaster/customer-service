package com.dili.customer.sdk.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/19 15:39
 */
@Data
public class MarketApprovalResultInput {

    /**
     * 审核的客户ID
     */
    @NotNull(message = "所属客户不能为空")
    private Long customerId;
    /**
     * 客户所属市场
     */
    @NotNull(message = "所属市场不能为空")
    private Long marketId;
    /**
     * 审核操作人
     */
    @NotNull(message = "审核操作人不能为空")
    private Long operatorId;

    /**
     * 审核结果是否通过
     */
    @NotNull(message = "是否通过不能为空")
    private Boolean passed;
    /**
     * 资料审核备注
     */
    @Size(max = 255, message = "审核备注请保持在255个以内")
    private String approvalNotes;
}

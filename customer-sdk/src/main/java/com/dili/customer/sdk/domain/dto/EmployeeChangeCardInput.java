package com.dili.customer.sdk.domain.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/7 17:01
 */
@Getter
@Setter
public class EmployeeChangeCardInput {

    /**
     * 客户ID
     */
    @NotNull(message = "所属客户不能为空")
    private Long customerId;

    /**
     * 卡账户ID
     */
    @NotNull(message = "卡账户不能为空")
    private Long cardAccountId;

    /**
     * 所属市场
     */
    @NotNull(message = "所属市场不能为空")
    private Long marketId;

    /**
     * 持卡人手机号
     */
    @NotBlank(message = "持卡人手机号不能为空")
    private String cellphone;

    /**
     * 持卡人手机号
     */
    @NotBlank(message = "新卡号不能为空")
    private String cardNo;
}

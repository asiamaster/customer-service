package com.dili.customer.sdk.domain.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/2 17:41
 */
@Getter
@Setter
public class EmployeeOpenCardInput extends EmployeeAddInput{

    /**
     * 园区卡账户ID
     */
    @NotNull(message = "园区卡账户不能为空")
    private Long cardAccountId;

    /**
     * 员工卡号
     */
    @NotBlank(message = "园区卡号不能为空")
    @Size(max = 20, message = "园区卡号请保持在20个字以内")
    private String cardNo;

    /**
     * 所属市场ID
     */
    @NotNull(message = "所属市场不能为空")
    private Long marketId;

}

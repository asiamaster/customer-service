package com.dili.customer.sdk.domain.dto;

import com.dili.customer.sdk.constants.ValidatedConstant;
import com.dili.customer.sdk.validator.AddView;
import com.dili.customer.sdk.validator.UpdateView;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 员工新增时的输入项
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/2 17:24
 */
@Getter
@Setter
public class EmployeeAddInput {

    /**
     * 所属的客户ID
     */
    @NotNull(message = "所属客户ID不能为空")
    private Long customerId;

    /**
     * 员工姓名
     */
    @NotBlank(message = "员工姓名不能为空")
    private String name;

    /**
     * 员工手机号
     */
    @NotBlank(message = "员工手机号不能为空")
    @Pattern(regexp = ValidatedConstant.CUSTOMER_CELLPHONE_VALID_REGEXP, message = "手机号格式不正确")
    private String cellphone;
}

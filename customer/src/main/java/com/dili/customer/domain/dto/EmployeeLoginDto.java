package com.dili.customer.domain.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 员工登录数据对象
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/7 16:42
 */
@Getter
@Setter
public class EmployeeLoginDto {

    /**
     * 登录账号(目前支持手机号或园区卡)
     */
    @NotBlank(message = "登录账号不能为空")
    private String userName;
    /**
     * 登录密码
     */
    @NotBlank(message = "登录密码能为空")
    private String password;
    /**
     * 所属客户
     */
    @NotNull(message = "所属客户不能为空")
    private Long customerId;
}

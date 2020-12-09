package com.dili.customer.sdk.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/5 15:49
 */
@Accessors(chain = true)
@Data
public class CustomerAutoRegisterDto {

    /**
     * 联系电话
     */
    @NotBlank(message = "注册手机号不能为空")
    @Pattern(regexp = "^(1[3456789]\\d{9})$", message = "请输入正确的联系方式")
    private String contactsPhone;

    /**
     * 注册密码
     */
    @NotBlank(message = "注册密码不能为空")
    private String password;

    /**
     * 来源系统
     * 需是在统一权限平台中-系统管理中，已有的系统
     */
    @NotBlank(message = "注册来源系统不能为空")
    private String sourceSystem;

    /**
     * 手机号是否已认证  1-是 0-否
     * {@link com.dili.commons.glossary.YesOrNoEnum}
     */
    @NotNull(message = "【是否已实名】不能为空")
    private Integer isCellphoneValid;

}

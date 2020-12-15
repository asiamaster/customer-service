package com.dili.customer.domain.wechat;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 微信注册实体类型
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/14 11:50
 */
@Data
public class WeChatRegisterDto {

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String cellphone;

    /**
     * 开放ID
     */
    @NotBlank(message = "微信openId不能为空")
    private String openId;
    /**
     * 头像
     */
    private String avatarUrl;
}

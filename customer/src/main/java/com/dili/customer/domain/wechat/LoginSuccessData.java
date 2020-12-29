package com.dili.customer.domain.wechat;

import com.dili.customer.domain.UserAccount;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 小程序登录成功后，返回的信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/14 18:37
 */
@Getter
@Setter
public class LoginSuccessData {

    /**
     * 登录后的token
     */
    private String accessToken;

    /**
     * 用户信息
     */
    private JsonObject userInfo;
}

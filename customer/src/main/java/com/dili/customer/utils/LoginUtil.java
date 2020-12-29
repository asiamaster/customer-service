package com.dili.customer.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dili.customer.domain.AccountTerminal;
import com.dili.customer.domain.UserAccount;
import com.dili.customer.domain.wechat.LoginSuccessData;
import com.dili.customer.sdk.constants.SecurityConstant;
import com.dili.customer.sdk.domain.dto.UserAccountJwtDto;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.Objects;

/**
 * 登录相关辅助类
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/28 19:18
 */
public class LoginUtil {

    /**
     * 组装返回登录成功后的数据
     * @param userAccount 登录成功的账号信息
     * @return
     */
    public static LoginSuccessData getLoginSuccessData(UserAccount userAccount, AccountTerminal accountTerminal) {
        String token = getToken(userAccount);
        userAccount.setPassword("");
        LoginSuccessData loginSuccessData = new LoginSuccessData();
        loginSuccessData.setAccessToken(token);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(userAccount));
        if (Objects.nonNull(accountTerminal)) {
            jsonObject.put("avatarUrl", accountTerminal.getAvatarUrl());
        }
        loginSuccessData.setUserInfo(jsonObject);
        return loginSuccessData;
    }

    /**
     * 登录后，获取token
     * @param info 账号信息
     * @return 生成的token
     */
    public static String getToken(UserAccount info) {
        UserAccountJwtDto jwtInfoDto = new UserAccountJwtDto();
        BeanUtils.copyProperties(info, jwtInfoDto);
        String token = JWT.create()
                //主题 放入用户名
                .withSubject(info.getAccountCode())
                //自定义属性 放入用户拥有请求权限
                .withClaim(SecurityConstant.JWT_CLAIMS_BODY, JSONUtil.toJsonStr(jwtInfoDto))
                //失效时间(先给个365天的失效时间)
                .withExpiresAt(DateUtil.offset(new Date(), DateField.YEAR, 1))
                //签名算法和密钥
                .sign(Algorithm.HMAC512(SecurityConstant.JWT_SIGN_KEY));
        return token;
    }
}

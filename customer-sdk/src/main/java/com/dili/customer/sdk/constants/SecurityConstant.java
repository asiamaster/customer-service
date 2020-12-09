package com.dili.customer.sdk.constants;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/8 16:12
 */
public interface SecurityConstant {

    /**
     * token参数头
     */
    String HEADER = "accessToken";

    /**
     * JWT签名加密key
     */
    String JWT_SIGN_KEY = "userCenter";

    /**
     * 登录时，存的用户密码
     */
    String PASSWORD_KEY = "passwordKey";

    /**
     * JWT 中自定义的属性对象key，最终以json字符串方式存储
     * 对象信息参考
     */
    String JWT_CLAIMS_BODY = "JwtClaimsBody";
}

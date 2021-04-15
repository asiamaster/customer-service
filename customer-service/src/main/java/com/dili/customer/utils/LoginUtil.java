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
import com.dili.ss.util.RSAUtils;
import org.springframework.beans.BeanUtils;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Objects;

/**
 * 登录相关辅助类
 *
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/28 19:18
 */
public class LoginUtil {

    private final static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCz4D01cJbbLdzUprznyrz4bueMWkLZNSBHuxXjynn4WnaELTidvA6280h7WHP+87iNmZAtvrmcEWGPCBvGrNRFzpqtN7c8h6E12SESVWjuF4VkH/tUN/F4UJLtNPEnsmmVAdarwn/c5RJqFVA2sFVlm6Zc2FV3QyPdrdMfa9AizwIDAQAB";
    private final static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALPgPTVwltst3NSmvOfKvPhu54xaQtk1IEe7FePKefhadoQtOJ28DrbzSHtYc/7zuI2ZkC2+uZwRYY8IG8as1EXOmq03tzyHoTXZIRJVaO4XhWQf+1Q38XhQku008SeyaZUB1qvCf9zlEmoVUDawVWWbplzYVXdDI92t0x9r0CLPAgMBAAECgYEAqCPLc4G8MkOLsmfuG0njHOMmpIbXCAzmEMcr7hOdse517JYM3z0kEBYXwdzsCP0vnYVXRbuL6vxAUqBEvpFdlhMYDNeDbKlqfWbvAa2RP6stib4OWR85gYbssRn3kh4IY1VWn+GeSbc5ztjSVXKnRbS+ezd0OmXJqiKzPpQtNMECQQDylOWkFeKgegAEzMXM/9VjjgXFoNb8AJVT8QXj2/m4ndL17/n4YHOwbMo0PDy69NKKMDAG3EnTNKBL0xIq2NMhAkEAvdNkMoI7Cedd35xG5bqB+GxWvrZPZN/QHhmQiUGO/CvslHL7QKeit4auDi30g3aUKbo07w/WfxL/me6yJRkn7wJAcXAtv0C4vOCwV45GxWmxqR+GFXf0cN349ssUPQzmR24OdBHnrD22e/8zw5+Tqr3IIvUL0Hl9UHYgq7Sln0HL4QJBAKn0u3Axg5SRb04GyL9kpnt63IuyBRGnBdn9P5h0dwW2egJLlENGE/zHe808PgD6SRu3GS+1eXGa2/jBawSmKkcCQGxLhtbCa08GrcQOHNYrtSfKRn+hJRKvwAWK4K64OGC94spgtPX5H3Ks3QxUGBWAtdlP+OVugfIfZ3Esim+2xSA=";
    static Algorithm algorithm = null;

    static {
        try {
            RSAPublicKey rsaPublicKey = RSAUtils.getPublicKey(publicKey);
            RSAPrivateKey rsaPrivateKey = RSAUtils.getPrivateKey(privateKey);
            algorithm = Algorithm.RSA512(rsaPublicKey, rsaPrivateKey);
        } catch (Exception e) {
        }
    }

    /**
     * 组装返回登录成功后的数据
     *
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
     *
     * @param info 账号信息
     * @return 生成的token
     */
    public static String getToken(UserAccount info) {
        UserAccountJwtDto jwtInfoDto = new UserAccountJwtDto();
        BeanUtils.copyProperties(info, jwtInfoDto);
        String token = JWT.create()
                //发布者
                .withIssuer(SecurityConstant.CUSTOMER_TOKEN_ISSUER)
                //发布时间
                .withIssuedAt(new Date())
                //主题 放入用户名
                .withSubject(info.getAccountCode())
                //自定义属性 放入用户拥有请求权限
                .withClaim(SecurityConstant.JWT_CLAIMS_BODY, JSONUtil.toJsonStr(jwtInfoDto))
                //失效时间(先给个365天的失效时间)
                .withExpiresAt(DateUtil.offset(new Date(), DateField.YEAR, 1))
                //签名算法和密钥
                .sign(algorithm);
        return token;
    }
}

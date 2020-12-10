package com.dili.customer.rpc;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.customer.constants.CustomerServiceConstant;
import com.dili.customer.utils.WeChatAppletAesUtil;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.redis.service.RedisUtil;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/8 17:53
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class WeChatRpc {

    private final RedisUtil redisUtil;

    /**
     * 微信接口主地址
     */
    @Value("${dili.wechat.base.url:https://api.weixin.qq.com}")
    private String wechatBaseUrl;

    /**
     * 小程序 AppId
     */
    @Value("${dili.wechat.applet.appId:}")
    private String appletAppId;

    /**
     * 小程序 secret
     */
    @Value("${dili.wechat.applet.secret:}")
    private String appletSecret;

    /**
     * 登录凭证校验
     *
     * @param code 临时登录凭证 code
     * @return BaseOutput
     */
    public BaseOutput<String> code2session(String code) {
        if (StringUtils.isBlank(code)) {
            return BaseOutput.failure("code不能为空");
        }
        try {
            String redisKey = CustomerServiceConstant.REDIS_KEY_PREFIX + "wechat:applet:" + code;
            Object o = redisUtil.get(redisKey);
            if (Objects.nonNull(o)) {
                return BaseOutput.successData(String.valueOf(o));
            }
            Map params = Maps.newHashMap();
            params.put("appid", appletAppId);
            params.put("secret", appletSecret);
            params.put("js_code", code);
            params.put("grant_type", "authorization_code");
            HttpResponse response = HttpUtil.createPost(wechatBaseUrl + "/sns/jscode2session").form(params).execute();
            log.info("code2session response << " + response);
            if (response.isOk()) {
                String responseBody = response.body();
                log.info("code2session responseBody << " + responseBody);
                JSONObject jsonObject = JSONObject.parseObject(responseBody);
                Integer errCode = jsonObject.getInteger("errcode");
                if (null == errCode || errCode == 0) {
                    String openId = jsonObject.getString("openid");
                    redisUtil.set(redisKey, openId, 5L, TimeUnit.MINUTES);
                    return BaseOutput.successData(openId);
                }
                return BaseOutput.failure(jsonObject.getString("errmsg")).setCode(String.valueOf(errCode));
            } else {
                response.close();
                log.error(String.format("根据code[%s]调用微信小程序失败，response状态码[%d]", code, response.getStatus()));
                return BaseOutput.failure("调用微信返回错误").setCode(String.valueOf(response.getStatus()));
            }
        } catch (Exception e) {
            log.error(String.format("根据code[%s]调用code2session异常[%s]！", code, e.getMessage()), e);
            return BaseOutput.failure("登录凭证校验异常");
        }
    }

    /**
     * 解密手机号信息
     *
     * @param sessionKey    会话密钥
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv            加密算法的初始向量
     * @return
     */
    public BaseOutput<AppletPhone> decodePhone(String sessionKey, String encryptedData, String iv) {
        if (StrUtil.isBlank(sessionKey) || StrUtil.isBlank(encryptedData) || StrUtil.isBlank(iv)) {
            return BaseOutput.failure("必要参数丢失");
        }
        log.info(String.format("解密手机号,原始值 == sessionKey:%s,encryptedData:%s,iv:%s", sessionKey, encryptedData, iv));
        try {
            sessionKey = URLDecoder.decode(sessionKey, "UTF-8");
            encryptedData = URLDecoder.decode(encryptedData, "UTF-8");
            iv = URLDecoder.decode(iv, "UTF-8");
            log.info(String.format("解密手机号,decode值 == sessionKey:%s,encryptedData:%s,iv:%s", sessionKey, encryptedData, iv));
            String decryStr = WeChatAppletAesUtil.decrypt(encryptedData, sessionKey, iv);
            log.info(String.format("解密后手机号信息:%s", decryStr));
            AppletPhone phone = JSONObject.parseObject(decryStr, AppletPhone.class);
            return BaseOutput.successData(phone);
        } catch (Exception e) {
            log.error("解密手机号码错误", e);
            return BaseOutput.failure("解密手机号码错误");
        }
    }
}

/**
 * 解密后的手机号信息对象
 */
@Data
class AppletPhone{
    /**
     * 用户绑定的手机号（国外手机号会有区号）
     */
    private String phoneNumber;
    /**
     * 没有区号的手机号
     */
    private String purePhoneNumber;
    /**
     * 区号
     */
    private String countryCode;

}

package com.dili.customer.rpc;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.customer.constants.CustomerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.redis.service.RedisUtil;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
            String redisKey = CustomerConstant.REDIS_KEY_PREFIX + "wechat:applet:" + code;
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
}

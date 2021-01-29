package com.dili.customer.rpc;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.dili.customer.domain.AppletInfo;
import com.dili.customer.domain.wechat.AppletRequestInfo;
import com.dili.customer.domain.wechat.JsCode2Session;
import com.dili.ss.domain.BaseOutput;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/8 17:53
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class WeChatRpc {

    private final AppletRequestInfo appletRequestInfo;

    /**
     * 微信接口主地址
     */
    @Value("${dili.wechat.base.url:https://api.weixin.qq.com}")
    private String wechatBaseUrl;


    /**
     * 登录凭证校验
     *
     * @param code 临时登录凭证 code
     * @return BaseOutput
     */
    public BaseOutput<JsCode2Session> code2session(String code) {
        if (StringUtils.isBlank(code)) {
            return BaseOutput.failure("code不能为空");
        }
        try {
            AppletInfo appletInfo = appletRequestInfo.getAppletInfo();
            Map params = Maps.newHashMap();
            params.put("appid", appletInfo.getAppId());
            params.put("secret", appletInfo.getSecret());
            params.put("js_code", code);
            params.put("grant_type", "authorization_code");
            HttpResponse response = HttpUtil.createPost(wechatBaseUrl + "/sns/jscode2session").form(params).execute();
            log.info("code2session response << " + response);
            if (response.isOk()) {
                String responseBody = response.body();
                log.info("code2session responseBody << " + responseBody);
                JsCode2Session jsCode2Session = JsCode2Session.fromJson(responseBody);
                if (Objects.isNull(jsCode2Session.getErrCode()) || jsCode2Session.getErrCode() == 0) {
                    log.info(String.format("根据临时code:%s 微信返回的session 为: %s", code, JSONUtil.toJsonStr(jsCode2Session)));
                    return BaseOutput.successData(jsCode2Session).setMetadata(appletInfo.getAppId());
                }
                return BaseOutput.failure(jsCode2Session.getErrMsg()).setCode(String.valueOf(jsCode2Session.getErrCode()));
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

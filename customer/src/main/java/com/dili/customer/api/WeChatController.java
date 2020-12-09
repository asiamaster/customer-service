package com.dili.customer.api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.dili.customer.rpc.WeChatRpc;
import com.dili.customer.service.UserAccountService;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 微信端服务接口
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/8 17:52
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/weChat")
public class WeChatController {

    private final WeChatRpc weChatRpc;
    private final UserAccountService userAccountService;

    /**
     * 小程序通过微信号登录凭证校验
     * @param code 微信小程序获取的code
     */
    @PostMapping(value = "/appletLogin")
    public BaseOutput login(@RequestParam("code") String code) {
        if (StrUtil.isBlank(code)) {
            return BaseOutput.failure("参数丢失").setCode(ResultCode.INVALID_REQUEST);
        }
        BaseOutput<String> baseOutput = weChatRpc.code2session(code);
        if (baseOutput.isSuccess()) {
            return userAccountService.loginByWechat(baseOutput.getData());
        }
        return baseOutput;
    }

    /**
     * 微信绑定
     * @param jsonObject json对象
     *                   code 微信小程序获取的code
     *                   cellphone  手机号
     *                   wechatAvatarUrl 头像图片地址
     * @param
     */
    @PostMapping(value = "/binding")
    public BaseOutput binding(@RequestBody JSONObject jsonObject) {
        String code = jsonObject.getStr("code");
        String cellphone = jsonObject.getStr("cellphone");
        String wechatAvatarUrl = jsonObject.getStr("wechatAvatarUrl");
        if (StrUtil.isBlank(code) || StrUtil.isBlank(cellphone) || StrUtil.isBlank(wechatAvatarUrl)) {
            return BaseOutput.failure("必要参数丢失");
        }
        BaseOutput<String> baseOutput = weChatRpc.code2session(code);
        if (baseOutput.isSuccess()) {
            return userAccountService.bindingWechat(baseOutput.getData(), cellphone, wechatAvatarUrl);
        }
        return baseOutput;
    }
}

package com.dili.customer.api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.dili.customer.annotation.AppletRequest;
import com.dili.customer.domain.wechat.AppletSystemInfo;
import com.dili.customer.domain.wechat.JsCode2Session;
import com.dili.customer.domain.wechat.WeChatRegisterDto;
import com.dili.customer.rpc.WeChatRpc;
import com.dili.customer.service.UserAccountService;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    private final AppletSystemInfo appletSystemInfo;

    /**
     * 微信号登录校验
     * @param code 微信小程序获取的临时code
     */
    @AppletRequest
    @PostMapping(value = "/appletLogin")
    public BaseOutput login(@RequestParam("code") String code) {
        if (StrUtil.isBlank(code)) {
            return BaseOutput.failure("参数丢失").setCode(ResultCode.INVALID_REQUEST);
        }
        BaseOutput<JsCode2Session> baseOutput = weChatRpc.code2session(code);
        if (baseOutput.isSuccess()) {
            return userAccountService.loginByWechat(baseOutput.getData().getOpenId());
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
    @AppletRequest
    @PostMapping(value = "/binding")
    public BaseOutput binding(@RequestBody JSONObject jsonObject) {
        String code = jsonObject.getStr("code");
        String cellphone = jsonObject.getStr("cellphone");
        String avatarUrl = jsonObject.getStr("avatarUrl");
        if (StrUtil.isBlank(code) || StrUtil.isBlank(cellphone) || StrUtil.isBlank(avatarUrl)) {
            return BaseOutput.failure("必要参数丢失");
        }
        BaseOutput<JsCode2Session> baseOutput = weChatRpc.code2session(code);
        if (baseOutput.isSuccess()) {
            return userAccountService.bindingWechat(baseOutput.getData().getOpenId(), cellphone, avatarUrl);
        }
        return baseOutput;
    }

    /**
     * 解密手机号
     * @param wxInfo
     * @return
     */
    @AppletRequest
    @PostMapping(value = "/decodePhone")
    public BaseOutput decodePhone(@RequestBody Map<String, String> wxInfo) {
        String sessionKey = wxInfo.get("sessionKey");
        String encryptedData = wxInfo.get("encryptedData");
        String iv = wxInfo.get("iv");
        return weChatRpc.decodePhone(sessionKey, encryptedData, iv);
    }

    /**
     * 登录凭证校验
     * @param code 微信小程序获取的code
     */
    @AppletRequest
    @PostMapping(value = "/code2session")
    public BaseOutput<JsCode2Session> code2session(@RequestParam("code") String code) {
        return weChatRpc.code2session(code);
    }


    /**
     * 微信一键注册
     * @param dto 注册信息
     * @return 注册成功后的信息
     */
    @AppletRequest
    @PostMapping(value = "/weChatRegister")
    public BaseOutput weChatRegister(@RequestBody WeChatRegisterDto dto) {
        return userAccountService.weChatRegister(dto, appletSystemInfo.getAppletSystem(), false);
    }

    /**
     * 微信一键注册并登录
     * @param dto 注册信息
     * @return 注册成功后的信息
     */
    @AppletRequest
    @PostMapping(value = "/registerAndLogin")
    public BaseOutput registerAndLogin(@RequestBody WeChatRegisterDto dto) {
        return userAccountService.weChatRegister(dto, appletSystemInfo.getAppletSystem(), true);
    }
}

package com.dili.customer.api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dili.customer.annotation.AppletRequest;
import com.dili.customer.domain.wechat.JsCode2Session;
import com.dili.customer.domain.wechat.LoginSuccessData;
import com.dili.customer.domain.wechat.WeChatRegisterDto;
import com.dili.customer.enums.AppletTerminalType;
import com.dili.customer.rpc.WeChatRpc;
import com.dili.customer.service.WeChatService;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

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
    private final WeChatService weChatService;

    /**
     * 微信号登录校验
     * @param code 微信小程序获取的临时code
     */
    @AppletRequest(appletType = AppletTerminalType.WE_CHAT)
    @PostMapping(value = "/appletLogin")
    public BaseOutput<LoginSuccessData> login(@RequestParam("code") String code) {
        if (StrUtil.isBlank(code)) {
            return BaseOutput.failure("参数丢失").setCode(ResultCode.INVALID_REQUEST);
        }
        BaseOutput<JsCode2Session> baseOutput = weChatRpc.code2session(code);
        if (baseOutput.isSuccess()) {
            return weChatService.loginByWechat(baseOutput.getData().getOpenId(), Objects.toString(baseOutput.getMetadata(), ""));
        }
        return BaseOutput.failure(baseOutput.getMessage()).setCode(baseOutput.getCode());
    }

    /**
     * 微信绑定
     * @param jsonObject json对象
     *                   code 微信小程序获取的code
     *                   cellphone  手机号
     *                   wechatAvatarUrl 头像图片地址
     * @param
     */
    @AppletRequest(appletType = AppletTerminalType.WE_CHAT)
    @PostMapping(value = "/binding")
    public BaseOutput binding(@RequestBody JSONObject jsonObject) {
        String code = jsonObject.getStr("code");
        String cellphone = jsonObject.getStr("cellphone");
        String avatarUrl = jsonObject.getStr("avatarUrl");
        String nickName = jsonObject.getStr("nickName");
        if (StrUtil.isBlank(code) || StrUtil.isBlank(cellphone)) {
            return BaseOutput.failure("必要参数丢失");
        }
        BaseOutput<JsCode2Session> baseOutput = weChatRpc.code2session(code);
        if (baseOutput.isSuccess()) {
            return weChatService.bindingWechat(baseOutput.getData().getOpenId(), cellphone, avatarUrl, Objects.toString(baseOutput.getMetadata(), ""), nickName);
        }
        return baseOutput;
    }

    /**
     * 解密手机号
     * @param wxInfo
     * @return
     */
    @AppletRequest(appletType = AppletTerminalType.WE_CHAT)
    @PostMapping(value = "/decodePhone")
    public BaseOutput decodePhone(@RequestBody Map<String, String> wxInfo) {
        String sessionKey = wxInfo.get("sessionKey");
        String encryptedData = wxInfo.get("encryptedData");
        String iv = wxInfo.get("iv");
        return weChatService.decodePhone(sessionKey, encryptedData, iv);
    }

    /**
     * 登录凭证校验
     * @param code 微信小程序获取的code
     */
    @AppletRequest(appletType = AppletTerminalType.WE_CHAT)
    @PostMapping(value = "/code2session")
    public BaseOutput<JsCode2Session> code2session(@RequestParam("code") String code) {
        return weChatRpc.code2session(code);
    }


    /**
     * 微信一键注册
     * @param dto 注册信息
     * @return 注册成功后的信息
     */
    @AppletRequest(appletType = AppletTerminalType.WE_CHAT)
    @PostMapping(value = "/weChatRegister")
    public BaseOutput weChatRegister(@RequestBody WeChatRegisterDto dto) {
        log.info(String.format("微信一键注册参数:%s", JSONUtil.toJsonStr(dto)));
        return weChatService.weChatRegister(dto, false);
    }

    /**
     * 微信一键注册并登录
     * @param dto 注册信息
     * @return 注册成功后的信息
     */
    @AppletRequest(appletType = AppletTerminalType.WE_CHAT)
    @PostMapping(value = "/registerAndLogin")
    public BaseOutput registerAndLogin(@RequestBody WeChatRegisterDto dto) {
        log.info(String.format("微信一键注册登录参数:%s", JSONUtil.toJsonStr(dto)));
        return weChatService.weChatRegister(dto, true);
    }
}

package com.dili.customer.api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dili.customer.service.UserAccountService;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 客户账号服务接口
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/8 17:48
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userAccount")
public class UserAccountController {

    private final UserAccountService userAccountService;

    /**
     * 根据手机号登录
     * @param cellphone 用户手机号
     * @param password 用户密码
     * @return 登录结果
     */
    @PostMapping("/loginByCellphone")
    public BaseOutput<JSONObject> loginByCellphone(@RequestParam("cellphone") String cellphone, @RequestParam("password") String password) {
        try {
            return userAccountService.loginByCellphone(cellphone, password);
        } catch (Exception e) {
            log.error(String.format("用户登录异常,手机号[%s],密码[%s],异常:%s", cellphone, password, e.getMessage()), e);
            return BaseOutput.failure("用户登录异常");
        }
    }

    /**
     * 账号更新登录密码
     * @param jsonString
     *  id 登录账号ID
     *  oldPassword 旧密码
     *  newPassword 新密码
     * @return 是否更新成功
     */
    @PostMapping("/changePassword")
    public BaseOutput<Boolean> changePassword(@RequestBody String jsonString) {
        if (StrUtil.isBlank(jsonString)) {
            return BaseOutput.failure("必要参数丢失").setCode(ResultCode.PARAMS_ERROR).setData(false);
        }
        try {
            JSONObject jsonObject = JSONUtil.parseObj(jsonString);
            Long id = jsonObject.getLong("id");
            String oldPassword = jsonObject.getStr("oldPassword");
            String newPassword = jsonObject.getStr("newPassword");
            return userAccountService.changePassword(id, oldPassword, newPassword);
        } catch (Exception e) {
            log.error(String.format("根据【%s】修改账户密码异常:", jsonString, e.getMessage()), e);
            return BaseOutput.failure("密码修改异常");
        }
    }
}

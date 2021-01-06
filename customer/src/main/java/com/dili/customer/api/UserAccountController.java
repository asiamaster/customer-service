package com.dili.customer.api;

import cn.hutool.json.JSONUtil;
import com.dili.customer.domain.wechat.LoginSuccessData;
import com.dili.customer.service.UserAccountService;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
    public BaseOutput<LoginSuccessData> loginByCellphone(@RequestParam("cellphone") String cellphone, @RequestParam("password") String password) {
        try {
            return userAccountService.loginByCellphone(cellphone, password);
        } catch (Exception e) {
            log.error(String.format("用户登录异常,手机号[%s],密码[%s],异常:%s", cellphone, password, e.getMessage()), e);
            return BaseOutput.failure("用户登录异常");
        }
    }

    /**
     * 账号更新登录密码
     * @param params 接口参数
     *  id 登录账号ID
     *  oldPassword 旧密码
     *  newPassword 新密码
     * @return 是否更新成功
     */
    @PostMapping("/changePassword")
    public BaseOutput<Boolean> changePassword(@RequestBody Map<String, Object> params) {
        log.info(String.format("更改账号密码参数为:%s", JSONUtil.toJsonStr(params)));
        try {
            Long id = Long.valueOf(Objects.toString(params.get("accountId"), "-1"));
            String oldPassword = Objects.toString(params.get("oldPassword"), null);
            String newPassword = Objects.toString(params.get("newPassword"), null);
            return userAccountService.changePassword(id, oldPassword, newPassword);
        } catch (Exception e) {
            log.error(String.format("根据【%s】修改账户密码异常:", JSONUtil.toJsonStr(params), e.getMessage()), e);
            return BaseOutput.failure("密码修改异常");
        }
    }

    /**
     * 根据证件号
     * @param certificateNumber 用户证件号
     * @param password 用户密码
     * @return 登录结果
     */
    @PostMapping("/loginByCertificateNumber")
    public BaseOutput<LoginSuccessData> loginByCertificateNumber(@RequestParam("certificateNumber") String certificateNumber, @RequestParam("password") String password) {
        try {
            return userAccountService.loginByCertificateNumber(certificateNumber, password);
        } catch (Exception e) {
            log.error(String.format("用户登录异常,证件号[%s],密码[%s],异常:%s", certificateNumber, password, e.getMessage()), e);
            return BaseOutput.failure("用户登录异常");
        }
    }

    /**
     * 账号重置登录密码
     * @param params 接口参数
     *  cellphone 手机号
     *  verificationCode 短信验证码
     *  newPassword 新密码
     * @return 是否更新成功
     */
    @PostMapping("/resetPassword")
    public BaseOutput<Boolean> resetPassword(@RequestBody Map<String, Object> params) {
        log.info(String.format("重置账号密码参数为:%s", JSONUtil.toJsonStr(params)));
        try {
            String cellphone = Objects.toString(params.get("cellphone"), null);
            String oldPassword = Objects.toString(params.get("verificationCode"), null);
            String newPassword = Objects.toString(params.get("newPassword"), null);
            Optional<String> s = userAccountService.resetPassword(cellphone, oldPassword, newPassword);
            if (s.isPresent()) {
                return BaseOutput.failure(s.get());
            }
            return BaseOutput.success();
        } catch (Exception e) {
            log.error(String.format("根据【%s】重置账户密码异常:", JSONUtil.toJsonStr(params), e.getMessage()), e);
            return BaseOutput.failure("密码重置异常");
        }
    }
}

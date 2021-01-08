package com.dili.customer.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.validation.BeanValidationResult;
import cn.hutool.extra.validation.ValidationUtil;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.commons.constants.CustomerResultCode;
import com.dili.customer.domain.AccountTerminal;
import com.dili.customer.domain.Customer;
import com.dili.customer.domain.UserAccount;
import com.dili.customer.domain.wechat.AppletPhone;
import com.dili.customer.domain.wechat.LoginSuccessData;
import com.dili.customer.domain.wechat.WeChatRegisterDto;
import com.dili.customer.enums.UserAccountEnum;
import com.dili.customer.utils.LoginUtil;
import com.dili.customer.utils.WeChatAppletAesUtil;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/28 19:15
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WeChatService {

    private final UserAccountService userAccountService;
    private final AccountTerminalService accountTerminalService;
    private final CustomerService customerService;

    /**
     * 微信一键注册
     * @param dto 微信注册信息
     * @param system 来源系统
     * @param login 注册后是否登录
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput weChatRegister(WeChatRegisterDto dto, String system, Boolean login) {
        if (Objects.isNull(dto)) {
            return BaseOutput.failure("必要参数丢失").setCode(ResultCode.PARAMS_ERROR);
        }
        BeanValidationResult beanValidationResult = ValidationUtil.warpValidate(dto);
        if (!beanValidationResult.isSuccess()) {
            return BaseOutput.failure(beanValidationResult.getErrorMessages().get(0).getMessage()).setCode(ResultCode.PARAMS_ERROR);
        }
        /**
         * 根据微信openId获取微信绑定账号
         */
        Optional<AccountTerminal> byAppAndTerminalCode = accountTerminalService.getByAppAndTerminalCode(dto.getAppId(), UserAccountEnum.AccountTerminalType.WE_CHAT, dto.getOpenId());
        if (byAppAndTerminalCode.isPresent()) {
            AccountTerminal accountTerminal = byAppAndTerminalCode.get();
            UserAccount userAccount = userAccountService.get(accountTerminal.getAccountId());
            if (login) {
                if (!userAccount.getIsEnable().equals(YesOrNoEnum.YES.getCode())) {
                    return BaseOutput.failure("用户为不可用状态,不能进行此操作").setCode(ResultCode.DATA_ERROR);
                }
                return BaseOutput.successData(LoginUtil.getLoginSuccessData(userAccount, accountTerminal));
            } else {
                if (!Objects.equals(dto.getCellphone(), userAccount.getCellphone())) {
                    return BaseOutput.failure("此微信已注册其它手机号").setCode(ResultCode.INVALID_REQUEST);
                }
                return BaseOutput.failure("请勿重复注册").setCode(CustomerResultCode.DUPLICATE_DATA);
            }
        }
        Optional<UserAccount> byCellphone = userAccountService.getByCellphone(dto.getCellphone());
        UserAccount userAccount = null;
        /**
         * 如果手机号已注册，则判断注册的
         */
        if (byCellphone.isPresent()) {
            userAccount = byCellphone.get();
        }
        /**
         * 终端号绑定为空，且用户账号也为空，则认为是新用户，未注册过
         */
        if (Objects.isNull(userAccount)) {
            Customer customer = new Customer();
            customer.setIsCellphoneValid(YesOrNoEnum.YES.getCode());
            customer.setContactsPhone(dto.getCellphone());
            customer.setSourceSystem(system);
            customer.setName(dto.getNickName());
            customerService.defaultRegister(customer);
            userAccount = new UserAccount();
            userAccount.setCellphone(dto.getCellphone()).setCustomerId(customer.getId()).setCustomerCode(customer.getCode())
                    .setCertificateNumber(customer.getCertificateNumber()).setCellphoneValid(YesOrNoEnum.YES.getCode());

        }
        if (StrUtil.isBlank(userAccount.getAccountName())) {
            userAccount.setAccountName(dto.getNickName());
        }
        userAccountService.insertOrUpdate(userAccount);
        //构建客户终端信息
        AccountTerminal accountTerminal = new AccountTerminal();
        accountTerminal.setNickName(dto.getNickName());
        accountTerminal.setAvatarUrl(dto.getAvatarUrl());
        accountTerminal.setTerminalCode(dto.getOpenId());
        accountTerminal.setAppId(dto.getAppId());
        accountTerminal.setTerminalType(UserAccountEnum.AccountTerminalType.WE_CHAT.getCode());
        accountTerminal.setCreateTime(LocalDateTime.now());
        accountTerminal.setAccountId(userAccount.getId());
        accountTerminal.setModifyTime(LocalDateTime.now());
        accountTerminalService.insert(accountTerminal);
        if (login) {
            if (!userAccount.getIsEnable().equals(YesOrNoEnum.YES.getCode())) {
                return BaseOutput.failure("用户为不可用状态,不能进行此操作").setCode(ResultCode.DATA_ERROR);
            }
            return BaseOutput.successData(LoginUtil.getLoginSuccessData(userAccount, accountTerminal));
        }
        return BaseOutput.success();
    }

    /**
     * 客户微信绑定
     * @param terminalCode 微信终端号
     * @param cellphone    客户手机号
     * @param wechatAvatarUrl 微信头像地址
     * @param appId 应用ID
     * @param nickName 终端号对应的昵称
     * @return
     */
    public BaseOutput bindingWechat(String terminalCode, String cellphone, String wechatAvatarUrl, String appId,String nickName) {
        Optional<AccountTerminal> byWechat = accountTerminalService.getByAppAndTerminalCode(appId, UserAccountEnum.AccountTerminalType.WE_CHAT, terminalCode);
        if (byWechat.isPresent()) {
            AccountTerminal accountTerminal = byWechat.get();
            UserAccount userAccount = userAccountService.get(accountTerminal.getAccountId());
            if (!userAccount.getCellphone().equals(cellphone)) {
                return BaseOutput.failure("该微信号已经绑定其他账号").setCode(ResultCode.FORBIDDEN);
            } else {
                //如果已和当前手机号绑定，则直接返回，认为已绑定
                return BaseOutput.failure("请勿重复绑定").setCode(CustomerResultCode.DUPLICATE_DATA);
            }
        }
        Optional<UserAccount> byCellphone = userAccountService.getByCellphone(cellphone);
        if (byCellphone.isEmpty()) {
            return BaseOutput.failure("对应的账号不存").setCode(ResultCode.PARAMS_ERROR);
        }
        UserAccount userAccount = byCellphone.get();
        AccountTerminal accountTerminal = new AccountTerminal();
        accountTerminal.setNickName(nickName);
        accountTerminal.setAvatarUrl(wechatAvatarUrl);
        accountTerminal.setTerminalCode(terminalCode);
        accountTerminal.setAppId(appId);
        accountTerminal.setTerminalType(UserAccountEnum.AccountTerminalType.WE_CHAT.getCode());
        accountTerminal.setCreateTime(LocalDateTime.now());
        accountTerminal.setAccountId(userAccount.getId());
        accountTerminal.setModifyTime(LocalDateTime.now());
        accountTerminalService.insert(accountTerminal);
        return BaseOutput.success();
    }

    /**
     * 根据微信登录
     * @param terminalCode 微信终端号
     * @param appId 应用ID
     * @return
     */
    public BaseOutput<LoginSuccessData> loginByWechat(String terminalCode, String appId) {
        Optional<AccountTerminal> byWechat = accountTerminalService.getByAppAndTerminalCode(appId, UserAccountEnum.AccountTerminalType.WE_CHAT, terminalCode);
        if (byWechat.isPresent()) {
            AccountTerminal accountTerminal = byWechat.get();
            UserAccount userAccount = userAccountService.get(accountTerminal.getAccountId());
            if (!userAccount.getIsEnable().equals(YesOrNoEnum.YES.getCode())) {
                return BaseOutput.failure("用户为不可用状态,不能进行此操作").setCode(ResultCode.DATA_ERROR);
            }
            return BaseOutput.successData(LoginUtil.getLoginSuccessData(userAccount, accountTerminal));
        } else {
            return BaseOutput.failure("未注册任何账号").setCode(ResultCode.NOT_FOUND);
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
            String decryStr = WeChatAppletAesUtil.decrypt(encryptedData, sessionKey, iv);
            log.info(String.format("解密后手机号信息:%s", decryStr));
            if (StrUtil.isBlank(decryStr)) {
                return BaseOutput.failure("解密手机号码为空");
            }
            AppletPhone phone = AppletPhone.fromJson(decryStr);
            return BaseOutput.successData(phone);
        } catch (Exception e) {
            log.error("解密手机号码错误", e);
            return BaseOutput.failure("解密手机号码错误");
        }
    }
}

package com.dili.customer.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.constants.CustomerServiceConstant;
import com.dili.customer.domain.Customer;
import com.dili.customer.domain.UserAccount;
import com.dili.customer.domain.wechat.LoginSuccessData;
import com.dili.customer.domain.wechat.WeChatRegisterDto;
import com.dili.customer.mapper.UserAccountMapper;
import com.dili.customer.sdk.constants.SecurityConstant;
import com.dili.customer.sdk.domain.dto.UserAccountJwtDto;
import com.dili.customer.service.CustomerService;
import com.dili.customer.service.UserAccountService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/8 14:48
 */
@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl extends BaseServiceImpl<UserAccount, Long> implements UserAccountService {

    private final UserAccountMapper userAccountMapper;
    @Autowired
    private CustomerService customerService;

    @Override
    public BaseOutput<Boolean> changePassword(Long id, String oldPassword, String newPassword) {
        if (Objects.isNull(id) || StrUtil.isBlank(oldPassword) || StrUtil.isBlank(newPassword)) {
            return BaseOutput.failure("必要参数丢失").setCode(ResultCode.PARAMS_ERROR).setData(false);

        }
        UserAccount userAccount = this.get(id);
        if (Objects.isNull(userAccount)) {
            return BaseOutput.failure("用户不存在").setCode(ResultCode.UNAUTHORIZED).setData(false);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(oldPassword, userAccount.getPassword())) {
            return BaseOutput.failure("原始密码不正确").setCode(ResultCode.DATA_ERROR).setData(false);
        }
        userAccount.setPassword(encoder.encode(newPassword));
        this.update(userAccount);
        return BaseOutput.successData(true);
    }

    @Override
    public BaseOutput<LoginSuccessData> loginByCellphone(String cellphone, String password) {
        if (StrUtil.isBlank(cellphone) && StrUtil.isBlank(password)) {
            return BaseOutput.failure("必要参数丢失").setCode(ResultCode.PARAMS_ERROR);
        }
        Optional<UserAccount> byCellphone = this.getByCellphone(cellphone);
        if (byCellphone.isEmpty()) {
            return BaseOutput.failure("账号或密码不正确").setCode(ResultCode.UNAUTHORIZED);
        }
        UserAccount info = byCellphone.get();
        if (!info.getIsEnable().equals(YesOrNoEnum.YES.getCode())) {
            return BaseOutput.failure("用户为不可用状态,不能进行此操作").setCode(ResultCode.DATA_ERROR);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, info.getPassword())) {
            return BaseOutput.failure("账号或密码不正确").setCode(ResultCode.UNAUTHORIZED);
        }
        return BaseOutput.successData(getLoginSuccessData(info));

    }

    @Override
    public void enableAll(Long[] ids) {
        if (ArrayUtil.isNotEmpty(ids)) {
            userAccountMapper.updateState(ids, YesOrNoEnum.YES.getCode());
        }
    }

    @Override
    public void disableAll(Long[] ids) {
        if (ArrayUtil.isNotEmpty(ids)) {
            userAccountMapper.updateState(ids, YesOrNoEnum.NO.getCode());
        }
    }

    @Override
    public void resetPassword(Long id) {
        UserAccount userAccount = this.get(id);
        if (Objects.nonNull(userAccount)) {
            setPassword(userAccount);
            this.update(userAccount);
        }
    }

    @Override
    public BaseOutput loginByWechat(String terminalCode) {
        Optional<UserAccount> byWechat = this.getByWechat(terminalCode);
        UserAccount userAccount = null;
        if (byWechat.isPresent()){
            userAccount = byWechat.get();
            if (!userAccount.getIsEnable().equals(YesOrNoEnum.YES.getCode())){
                return BaseOutput.failure("用户为不可用状态,不能进行此操作").setCode(ResultCode.DATA_ERROR);
            }
        } else {
            return BaseOutput.failure("未注册任何账号").setCode(ResultCode.NOT_FOUND);
        }
        return BaseOutput.successData(getLoginSuccessData(userAccount));
    }

    @Override
    public BaseOutput bindingWechat(String terminalCode, String cellphone, String wechatAvatarUrl) {
        Optional<UserAccount> byWechat = this.getByWechat(terminalCode);
        if (byWechat.isPresent()) {
            UserAccount userAccount = byWechat.get();
            if (!userAccount.getCellphone().equals(cellphone)) {
                return BaseOutput.failure("该微信号已经绑定其他账号").setCode(ResultCode.FORBIDDEN);
            } else {
                //如果已和当前手机号绑定，则直接返回，认为已绑定
                return BaseOutput.success();
            }
        }
        Optional<UserAccount> byCellphone = this.getByCellphone(cellphone);
        if (byCellphone.isEmpty()) {
            return BaseOutput.failure("对应的账号不存").setCode(ResultCode.PARAMS_ERROR);
        }
        UserAccount userAccount = byCellphone.get();
        userAccount.setWechatTerminalCode(terminalCode);
        if (StrUtil.isBlank(userAccount.getAvatarUrl())) {
            userAccount.setAvatarUrl(wechatAvatarUrl);
        }
        this.update(userAccount);
        return BaseOutput.success();
    }

    /**
     * 根据手机号查询已验证过该手机号的用户账户信息
     * @param cellphone 手机号
     * @return
     */
    @Override
    public Optional<UserAccount> getByCellphone(String cellphone) {
        if (StrUtil.isBlank(cellphone)) {
            return Optional.empty();
        }
        UserAccount queryCondition = new UserAccount();
        queryCondition.setCellphone(cellphone);
        queryCondition.setCellphoneValid(YesOrNoEnum.YES.getCode());
        List<UserAccount> accountList = this.list(queryCondition);
        return accountList.stream().findFirst();
    }

    @Override
    public void add(UserAccount userAccount) {
        produceSaveData(userAccount);
        insert(userAccount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insertOrUpdate(UserAccount userAccount) {
        if (Objects.isNull(userAccount)) {
            return 0;
        }
        produceSaveData(userAccount);
        return saveOrUpdate(userAccount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseOutput weChatRegister(WeChatRegisterDto dto, String system, Boolean login) {
        if (Objects.isNull(dto)) {
            return BaseOutput.failure("必要参数丢失").setCode(ResultCode.PARAMS_ERROR);
        }
        /**
         * 根据微信openId获取微信绑定账号
         */
        Optional<UserAccount> byWechat = this.getByWechat(dto.getOpenId());
        if (byWechat.isPresent()) {
            UserAccount userAccount = byWechat.get();
            if (StrUtil.isBlank(userAccount.getAvatarUrl())) {
                userAccount.setAvatarUrl(dto.getAvatarUrl());
            }
            if (StrUtil.isBlank(userAccount.getAccountName())) {
                userAccount.setAccountName(dto.getNickName());
            }
            this.update(userAccount);
            if (login) {
                if (!userAccount.getIsEnable().equals(YesOrNoEnum.YES.getCode())) {
                    return BaseOutput.failure("用户为不可用状态,不能进行此操作").setCode(ResultCode.DATA_ERROR);
                }
                return BaseOutput.successData(getLoginSuccessData(userAccount));
            }
            return BaseOutput.successData(true);
        }
        Optional<UserAccount> byCellphone = this.getByCellphone(dto.getCellphone());
        UserAccount userAccount = null;
        /**
         * 如果手机号已注册，则判断注册的
         */
        if (byCellphone.isPresent()) {
            userAccount = byCellphone.get();
            //如果此账号已绑定微信终端，则需要判断绑定的微信终端是否为当前传入
            if (StrUtil.isNotBlank(userAccount.getWechatTerminalCode())) {
                return BaseOutput.failure("此手机号已绑定其它终端").setData(false).setCode(ResultCode.DATA_ERROR);
            }
        }
        /**
         * 终端号绑定为空，且用户账号也为空，则认为是新用户，未注册过
         */
        if (Objects.isNull(userAccount)) {
            BaseOutput<Customer> customerBaseOutput = customerService.insertByContactsPhone(dto.getCellphone(), system);
            if (customerBaseOutput.isSuccess()) {
                Customer customer = customerBaseOutput.getData();
                userAccount = new UserAccount();
                userAccount.setAccountCode(dto.getCellphone()).setCellphone(dto.getCellphone()).setCustomerId(customer.getId()).setCustomerCode(customer.getCode())
                        .setCertificateNumber(customer.getCertificateNumber()).setCellphoneValid(YesOrNoEnum.YES.getCode());
                produceSaveData(userAccount);
            } else {
                return BaseOutput.failure(customerBaseOutput.getMessage()).setData(false);
            }
        }
        if (StrUtil.isBlank(userAccount.getAvatarUrl())) {
            userAccount.setAvatarUrl(dto.getAvatarUrl());
        }
        if (StrUtil.isBlank(userAccount.getAccountName())) {
            userAccount.setAccountName(dto.getNickName());
        }
        userAccount.setModifyTime(LocalDateTime.now());
        userAccount.setWechatTerminalCode(dto.getOpenId());
        this.saveOrUpdate(userAccount);
        if (login) {
            if (!userAccount.getIsEnable().equals(YesOrNoEnum.YES.getCode())) {
                return BaseOutput.failure("用户为不可用状态,不能进行此操作").setCode(ResultCode.DATA_ERROR);
            }
            return BaseOutput.successData(getLoginSuccessData(userAccount));
        }
        return BaseOutput.successData(true);
    }

    @Override
    public BaseOutput<LoginSuccessData> loginByCertificateNumber(String certificateNumber, String password) {
        if (StrUtil.isBlank(certificateNumber) || StrUtil.isBlank(password)) {
            return BaseOutput.failure("必要参数丢失").setCode(ResultCode.PARAMS_ERROR);
        }
        UserAccount condition = new UserAccount();
        condition.setCertificateNumber(certificateNumber);
        Optional<UserAccount> optional = this.list(condition).stream().findFirst();
        if (optional.isEmpty()) {
            return BaseOutput.failure("账号或密码不正确").setCode(ResultCode.UNAUTHORIZED);
        }
        UserAccount info = optional.get();
        if (!info.getIsEnable().equals(YesOrNoEnum.YES.getCode())) {
            return BaseOutput.failure("用户为不可用状态,不能进行此操作").setCode(ResultCode.DATA_ERROR);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, info.getPassword())) {
            return BaseOutput.failure("账号或密码不正确").setCode(ResultCode.UNAUTHORIZED);
        }
        return BaseOutput.successData(getLoginSuccessData(info));
    }

    @Override
    public Optional<UserAccount> getByCustomerId(Long customerId) {
        UserAccount condition = new UserAccount();
        condition.setCustomerId(customerId);
        return list(condition).stream().findFirst();
    }

    /**
     * 组装返回登录成功后的数据
     * @param userAccount 登录成功的账号信息
     * @return
     */
    private LoginSuccessData getLoginSuccessData(UserAccount userAccount) {
        String token = getToken(userAccount);
        userAccount.setPassword("");
        LoginSuccessData loginSuccessData = new LoginSuccessData();
        loginSuccessData.setAccessToken(token);
        loginSuccessData.setUserInfo(userAccount);
        return loginSuccessData;
    }

    /**
     * 根据微信终端号，获取账号信息
     * @param terminalCode 微信终端号
     * @return
     */
    private Optional<UserAccount> getByWechat(String terminalCode) {
        if (StrUtil.isBlank(terminalCode)) {
            return Optional.empty();
        }
        UserAccount queryCondition = new UserAccount();
        queryCondition.setWechatTerminalCode(terminalCode);
        List<UserAccount> accountList = this.list(queryCondition);
        return accountList.stream().findFirst();
    }

    /**
     * 生成默认保存数据
     * @param saveData
     */
    private void produceSaveData(UserAccount saveData) {
        saveData.setModifyTime(LocalDateTime.now());
        if (Objects.isNull(saveData.getId())) {
            setPassword(saveData);
            saveData.setIsEnable(YesOrNoEnum.YES.getCode());
            saveData.setCreateTime(saveData.getModifyTime());
            saveData.setAccountCode("c_" + IdUtil.getSnowflake(1, 1).nextIdStr());
        }
    }

    /**
     * 根据对象数据，按照规则设置数据密码
     * @param saveData
     */
    private void setPassword(UserAccount saveData) {
        saveData.setPassword(new BCryptPasswordEncoder().encode(CustomerServiceConstant.DEFAULT_PASSWORD));
    }

    /**
     * 登录后，获取token
     * @param info 账号信息
     * @return 生成的token
     */
    private String getToken(UserAccount info) {
        UserAccountJwtDto jwtInfoDto = new UserAccountJwtDto();
        BeanUtils.copyProperties(info, jwtInfoDto);
        String token = JWT.create()
                //主题 放入用户名
                .withSubject(info.getAccountCode())
                //自定义属性 放入用户拥有请求权限
                .withClaim(SecurityConstant.JWT_CLAIMS_BODY, JSONUtil.toJsonStr(jwtInfoDto))
                //失效时间(先给个365天的失效时间)
                .withExpiresAt(DateUtil.offset(new Date(), DateField.YEAR, 1))
                //签名算法和密钥
                .sign(Algorithm.HMAC512(SecurityConstant.JWT_SIGN_KEY));
        return token;
    }
}

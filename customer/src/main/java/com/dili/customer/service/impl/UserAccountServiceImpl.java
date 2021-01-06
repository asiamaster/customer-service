package com.dili.customer.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.commons.constants.CustomerConstant;
import com.dili.customer.commons.service.CommonDataService;
import com.dili.customer.constants.CustomerServiceConstant;
import com.dili.customer.domain.UserAccount;
import com.dili.customer.domain.wechat.LoginSuccessData;
import com.dili.customer.mapper.UserAccountMapper;
import com.dili.customer.service.UserAccountService;
import com.dili.customer.utils.LoginUtil;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/8 14:48
 */
@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl extends BaseServiceImpl<UserAccount, Long> implements UserAccountService {

    private final UserAccountMapper userAccountMapper;
    private final CommonDataService commonDataService;

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
        userAccount.setChangedPwdTime(LocalDateTime.now());
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
        return BaseOutput.successData(LoginUtil.getLoginSuccessData(info,null));
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
    public Optional<String> resetPassword(String cellphone, String verificationCode, String newPassword) {
        if (StrUtil.isBlank(cellphone) || StrUtil.isBlank(verificationCode) || StrUtil.isBlank(newPassword)) {
            return Optional.of("必要参数丢失");
        }
        if (!PhoneUtil.isMobile(cellphone)) {
            return Optional.of("手机号格式不正确");
        }
        Optional<UserAccount> byCellphone = this.getByCellphone(cellphone);
        if (byCellphone.isEmpty()) {
            return Optional.of("账号不存在");
        }
        Optional<String> s = commonDataService.checkVerificationCode(cellphone, CustomerConstant.RESET_AUTH_CODE, verificationCode);
        if (s.isPresent()) {
            return s;
        }
        UserAccount userAccount = byCellphone.get();
        userAccount.setPassword(newPassword);
        setPassword(userAccount);
        this.update(userAccount);
        return Optional.empty();
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
        queryCondition.setDeleted(YesOrNoEnum.NO.getCode());
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
    public BaseOutput<LoginSuccessData> loginByCertificateNumber(String certificateNumber, String password) {
        if (StrUtil.isBlank(certificateNumber) || StrUtil.isBlank(password)) {
            return BaseOutput.failure("必要参数丢失").setCode(ResultCode.PARAMS_ERROR);
        }
        UserAccount condition = new UserAccount();
        condition.setCertificateNumber(certificateNumber);
        condition.setDeleted(YesOrNoEnum.NO.getCode());
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
        return BaseOutput.successData(LoginUtil.getLoginSuccessData(info, null));
    }

    @Override
    public Optional<UserAccount> getByCustomerId(Long customerId) {
        UserAccount condition = new UserAccount();
        condition.setCustomerId(customerId);
        condition.setDeleted(YesOrNoEnum.NO.getCode());
        return list(condition).stream().findFirst();
    }


    /**
     * 生成默认保存数据
     * @param saveData
     */
    private void produceSaveData(UserAccount saveData) {
        if (Objects.isNull(saveData.getId())) {
            setPassword(saveData);
            saveData.setIsEnable(YesOrNoEnum.YES.getCode());
            saveData.setModifyTime(LocalDateTime.now());
            saveData.setCreateTime(saveData.getModifyTime());
            saveData.setAccountCode("c_" + IdUtil.getSnowflake(1, 1).nextIdStr());
            saveData.setDeleted(YesOrNoEnum.NO.getCode());
            saveData.setChangedPwdTime(saveData.getModifyTime());
        }
    }

    /**
     * 根据对象数据，按照规则设置数据密码
     * @param saveData
     */
    private void setPassword(UserAccount saveData) {
        //如果存在输入密码的情况，则直接加密输入的密码
        if (StrUtil.isNotBlank(saveData.getPassword())) {
            saveData.setPassword(new BCryptPasswordEncoder().encode(saveData.getPassword()));
        } else {
            saveData.setPassword(new BCryptPasswordEncoder().encode(CustomerServiceConstant.DEFAULT_PASSWORD));
        }
        saveData.setChangedPwdTime(LocalDateTime.now());
    }
}

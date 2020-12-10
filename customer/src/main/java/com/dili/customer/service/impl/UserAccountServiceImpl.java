package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.constants.CustomerServiceConstant;
import com.dili.customer.domain.UserAccount;
import com.dili.customer.mapper.UserAccountMapper;
import com.dili.customer.sdk.constants.SecurityConstant;
import com.dili.customer.sdk.domain.dto.UserAccountJwtDto;
import com.dili.customer.service.UserAccountService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
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

    @Override
    public BaseOutput<Boolean> changePassword(Long id, String oldPassword, String newPassword) {
        if (Objects.nonNull(id) && StrUtil.isNotBlank(oldPassword) && StrUtil.isNotBlank(newPassword)) {
            UserAccount userAccount = this.get(id);
            if (Objects.nonNull(userAccount)) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                if (!encoder.matches(oldPassword, userAccount.getPassword())) {
                    return BaseOutput.failure("原始密码不正确").setCode(ResultCode.DATA_ERROR).setData(false);
                }
                userAccount.setPassword(encoder.encode(newPassword));
                this.update(userAccount);
            }
            return BaseOutput.failure("用户不存在").setCode(ResultCode.UNAUTHORIZED).setData(false);

        }
        return BaseOutput.failure("必要参数丢失").setCode(ResultCode.PARAMS_ERROR).setData(false);
    }

    @Override
    public BaseOutput<JSONObject> loginByCellphone(String cellphone, String password) {
        if (Objects.nonNull(cellphone) && Objects.nonNull(password)) {
            Optional<UserAccount> byCellphone = this.getByCellphone(cellphone);
            if (byCellphone.isPresent()) {
                UserAccount info = byCellphone.get();
                if (!info.getIsEnable().equals(YesOrNoEnum.YES.getCode())) {
                    return BaseOutput.failure("用户为不可用状态,不能进行此操作").setCode(ResultCode.DATA_ERROR);
                }
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                if (!encoder.matches(password, info.getPassword())) {
                    return BaseOutput.failure("账号或密码不正确").setCode(ResultCode.UNAUTHORIZED);
                }
                String token = getToken(info);
                JSONObject object = new JSONObject();
                info.setPassword("");
                object.set("userInfo", info);
                object.set("accessToken", token);
                return BaseOutput.successData(object);
            }
            return BaseOutput.failure("账号或密码不正确").setCode(ResultCode.UNAUTHORIZED);
        }
        return BaseOutput.failure("必要参数丢失").setCode(ResultCode.PARAMS_ERROR);
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
        UserAccount query = new UserAccount();
        query.setWechatTerminalCode(terminalCode);
        List<UserAccount> list = this.list(query);
        UserAccount userAccount = null;
        if (CollectionUtil.isNotEmpty(list)){
            userAccount = list.get(0);
            if (!userAccount.getIsEnable().equals(YesOrNoEnum.YES.getCode())){
                return BaseOutput.failure("用户为不可用状态,不能进行此操作").setCode(ResultCode.DATA_ERROR);
            }
        } else {
            return BaseOutput.failure("未绑定任何账号").setCode(ResultCode.NOT_FOUND);
        }
        String token = getToken(userAccount);
        JSONObject object = new JSONObject();
        userAccount.setPassword("");
        object.set("userInfo", userAccount);
        object.set("accessToken", token);
        return BaseOutput.successData(object);
    }

    @Override
    public BaseOutput bindingWechat(String terminalCode, String cellphone, String wechatAvatarUrl) {
        UserAccount query = new UserAccount();
        query.setWechatTerminalCode(terminalCode);
        List<UserAccount> list = this.list(query);
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.size() > 1) {
                return BaseOutput.failure("终端已绑定多个设备").setCode(ResultCode.DATA_ERROR);
            }
            UserAccount userAccount = list.get(0);
            if (!userAccount.getCellphone().equals(cellphone)) {
                return BaseOutput.failure("该微信号已经绑定其他账号").setCode(ResultCode.FORBIDDEN);
            }
        }
        Optional<UserAccount> byCellphone = this.getByCellphone(cellphone);
        if (byCellphone.isEmpty()) {
            return BaseOutput.failure("对应的账号不存").setCode(ResultCode.PARAMS_ERROR);
        }
        UserAccount userAccount = byCellphone.get();
        userAccount.setWechatTerminalCode(terminalCode);
        if (StrUtil.isBlank(userAccount.getAvatarUrl())){
            userAccount.setAvatarUrl(wechatAvatarUrl);
        }
        this.update(userAccount);
        return BaseOutput.success();
    }

    /**
     * 根据手机号查询用户账户信息
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
        List<UserAccount> accountList = this.list(queryCondition);
        return accountList.stream().findFirst();
    }

    @Override
    public void add(UserAccount userAccount) {
        produceSaveData(userAccount);
        insert(userAccount);
    }

    /**
     * 生成默认保存数据
     * @param saveData
     */
    private void produceSaveData(UserAccount saveData) {
        setPassword(saveData);
        saveData.setIsEnable(YesOrNoEnum.YES.getCode());
        saveData.setModifyTime(LocalDateTime.now());
        saveData.setCreateTime(saveData.getModifyTime());
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

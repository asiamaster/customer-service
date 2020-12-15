package com.dili.customer.service;

import com.dili.customer.domain.UserAccount;
import com.dili.customer.domain.wechat.LoginSuccessData;
import com.dili.customer.domain.wechat.WeChatRegisterDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

import java.util.Optional;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/8 14:44
 */
public interface UserAccountService extends BaseService<UserAccount, Long> {

    /**
     * 根据账号更新登录密码
     * @param id 登录账号的ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否更新成功
     */
    BaseOutput<Boolean> changePassword(Long id,String oldPassword, String newPassword);

    /**
     * 用户登录
     * @param cellphone 用户手机号
     * @param password 登录密码
     * @return
     */
    BaseOutput<LoginSuccessData> loginByCellphone(String cellphone, String password);

    /**
     * 多选启用
     * @param ids
     */
    void enableAll(Long[] ids);

    /**
     * 多选禁用
     * @param ids
     */
    void disableAll(Long[] ids);

    /**
     * 根据ID重置账号密码
     * @param id 需要重置密码的数据ID
     */
    void resetPassword(Long id);

    /**
     * 根据微信登录
     * @param terminalCode 微信终端号
     * @return
     */
    BaseOutput<LoginSuccessData> loginByWechat(String terminalCode);

    /**
     * 客户微信绑定
     * @param terminalCode 微信终端号
     * @param cellphone    客户手机号
     * @param wechatAvatarUrl 微信头像地址
     * @return
     */
    BaseOutput bindingWechat(String terminalCode, String cellphone, String wechatAvatarUrl);

    /**
     * 根据手机号查询用户账户信息
     * 只会查询此手机号被验证过的对应账号
     * @param cellphone 手机号
     * @return
     */
    Optional<UserAccount> getByCellphone(String cellphone);

    /**
     * 增加账号主信息
     * @param userAccount
     */
    void add(UserAccount userAccount);

    /**
     * 新增或修改用户账号
     * @param userAccount
     * @return
     */
    Integer insertOrUpdate(UserAccount userAccount);

    /**
     * 微信一键注册
     * @param dto 微信注册信息
     * @param system 来源系统
     * @param login 注册后是否登录
     * @return
     */
    BaseOutput weChatRegister(WeChatRegisterDto dto, String system, Boolean login);

    /**
     * 根据证件号加密码登录
     * @param certificateNumber 客户证件号
     * @param password 用户密码
     * @return
     */
    BaseOutput<LoginSuccessData> loginByCertificateNumber(String certificateNumber, String password);

    /**
     * 根据客户获取客户的账号信息
     * @param customerId 客户
     * @return
     */
    Optional<UserAccount> getByCustomerId(Long customerId);

    /**
     * 组装返回登录成功后的数据
     * @param userAccount 登录成功的账号信息
     * @return
     */
    LoginSuccessData getLoginSuccessData(UserAccount userAccount);
}

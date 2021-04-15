package com.dili.customer.service;

import com.dili.customer.domain.Customer;
import com.dili.customer.sdk.domain.dto.*;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-12-30 11:18:34.
 * @author yuehongbo
 */
public interface CustomerManageService extends BaseService<Customer, Long> {

    /**
     * 保存客户基本信息
     * @param baseInfo
     * @return
     */
    BaseOutput<Customer> register(EnterpriseCustomerInput baseInfo);

    /**
     * 客户信息更新
     * @param updateInput 需要更新的数据
     * @return
     */
    BaseOutput update(CustomerUpdateInput updateInput);

    /**
     * 客户自动注册
     * @param dto
     * @return
     */
    BaseOutput<Customer> autoRegister(CustomerAutoRegisterDto dto);

    /**
     * 完善客户信息
     * @param input
     * @return
     */
    BaseOutput<Customer> completeInfo(CustomerUpdateInput input);

    /**
     * 批量完善个人客户信息
     * @param inputList
     * @return
     */
    BaseOutput<Long> batchCompleteIndividual(List<CustomerUpdateInput> inputList);

    /**
     * 批量完善企业客户信息
     * @param inputList
     * @return
     */
    BaseOutput<Long> batchCompleteEnterprise(List<CustomerUpdateInput> inputList);

    /**
     * 根据联系电话自动注册，此方法只会创建客户，不会创建对应的账号
     * @param contactsPhone 联系电话
     * @param sourceSystem 来源系统
     * @param name 客户名称
     * @return
     */
    BaseOutput<Customer> insertByContactsPhone(String contactsPhone, String sourceSystem, String name);

    /**
     * 更改客户-手机号验证
     * @param customerId 客户ID
     * @param cellphone 手机号
     * @return
     */
    String updateCellphoneValid(Long customerId, String cellphone);

    /**
     * 默认注册实现
     * @param customer
     */
    void defaultRegister(Customer customer);

    /**
     * 更改客户基本信息
     * @param input 客户基本信息
     * @return
     */
    BaseOutput updateBaseInfo(CustomerBaseUpdateInput input);

    /**
     * 更改客户基本信息
     * @param input    客户基本信息
     * @param isLogger 是否需要记录操作日志
     * @return
     */
    BaseOutput updateBaseInfo(CustomerBaseUpdateInput input, Boolean isLogger);

    /**
     * 更改客户证件信息
     * @param input 证件信息
     * @param isLogger 是否需要记录操作日志
     * @return
     */
    Optional<String> updateCertificateInfo(CustomerCertificateInput input, Boolean isLogger);

    /**
     * 根据客户ID逻辑删除客户数据
     * @param customerId 客户ID
     */
    void logicDelete(Long customerId);

    /**
     * 验证客户手机号
     * @param cellphone 手机号
     * @param customerId  客户ID
     * @param verificationCode 验证码
     * @return
     */
    Optional<String> verificationCellPhone(String cellphone, Long customerId, String verificationCode);

    /**
     * 逐条发送消息
     * @param exchange
     * @param customerId
     * @param marketId
     */
    void asyncSendCustomerToMq(String exchange, Long customerId, Long marketId);

    /**
     * 批量发送消息
     * @param exchange
     * @param customerId
     * @param marketIds
     */
    void asyncSendCustomerToMq(String exchange, Long customerId, Set<Long> marketIds);
}
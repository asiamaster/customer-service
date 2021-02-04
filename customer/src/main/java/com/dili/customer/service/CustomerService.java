package com.dili.customer.service;

import com.dili.customer.domain.Customer;
import com.dili.customer.sdk.domain.dto.*;
import com.dili.customer.sdk.domain.query.CustomerBaseQueryInput;
import com.dili.customer.sdk.domain.query.CustomerQueryInput;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;

import java.util.List;
import java.util.Optional;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-12-30 11:18:34.
 * @author yuehongbo
 */
public interface CustomerService extends BaseService<Customer, Long> {

    /**
     * 根据证件号获取客户的基本信息
     * 此方法只会单表查询Customer表
     * @param certificateNumber 证件号
     * @return
     */
    Customer getBaseInfoByCertificateNumber(String certificateNumber);

    /**
     * 保存客户基本信息
     * @param baseInfo
     * @return
     */
    BaseOutput<Customer> register(EnterpriseCustomerInput baseInfo);

    /**
     * 分页查询客户信息
     * 此方法只会简单的返回客户及市场信息数据，不会返回其它关联对象数据
     * @param input
     * @return
     */
    PageOutput<List<Customer>> listSimpleForPage(CustomerQueryInput input);

    /**
     * 分页查询客户信息
     * @param input
     * @return
     */
    PageOutput<List<Customer>> listForPage(CustomerQueryInput input);

    /**
     * 分页查询客户基本信息，不带有任何市场属性数据
     * @param input
     * @return
     */
    PageOutput<List<Customer>> listBasePage(CustomerBaseQueryInput input);

    /**
     * 根据证件号检测某个客户在某市场是否已存在
     * @param certificateNumber 证件号
     * @param marketId 市场ID
     * @return 如果客户在当前市场已存在，则返回错误(false)信息，如果不存在，则返回客户信息(若客户信息存在)
     */
    BaseOutput<Customer> checkExistByNoAndMarket(String certificateNumber,Long marketId);

    /**
     * 客户信息更新
     * @param updateInput 需要更新的数据
     * @return
     */
    BaseOutput update(CustomerUpdateInput updateInput);

    /**
     * 获取被某手机号验证的客户
     * @param cellphone 手机号
     * @return
     */
    List<Customer> getValidatedCellphoneCustomer(String cellphone);

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
     * 获取客户及对应所在市场的详细数据信息
     * @param id 客户ID
     * @param marketId 市场ID
     * @return
     */
    Customer get(Long id, Long marketId);

    /**
     * 根据客户ID逻辑删除客户数据
     * @param customerId 客户ID
     */
    void logicDelete(Long customerId);

}
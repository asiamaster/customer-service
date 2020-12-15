package com.dili.customer.service;

import com.dili.customer.domain.Customer;
import com.dili.customer.sdk.domain.dto.*;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
    BaseOutput<Customer> update(CustomerUpdateInput updateInput);

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
     * 根据联系电话自动注册，此方法只会创建客户，不会创建对应的账号
     * @param contactsPhone 联系电话
     * @param sourceSystem 来源系统
     * @return
     */
    BaseOutput<Customer> insertByContactsPhone(String contactsPhone, String sourceSystem);

    /**
     * 更改客户-手机号验证
     * @param customerId 客户ID
     * @param cellphone 手机号
     * @return
     */
    String updateCellphoneValid(Long customerId, String cellphone);
}
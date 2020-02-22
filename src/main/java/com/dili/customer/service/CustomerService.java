package com.dili.customer.service;

import com.dili.customer.domain.Customer;
import com.dili.customer.domain.dto.CustomerCertificateInfoInput;
import com.dili.customer.domain.dto.CustomerQueryInput;
import com.dili.customer.domain.dto.EnterpriseCustomerInput;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;

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
    BaseOutput saveBaseInfo(EnterpriseCustomerInput baseInfo);

    /**
     * 保存客户证件相关新
     * @param certificateInfo
     * @return
     */
    BaseOutput saveCertificateInfo(CustomerCertificateInfoInput certificateInfo);

    /**
     * 分页查询客户信息
     * @param input
     * @return
     */
    PageOutput listForPage(CustomerQueryInput input);

    /**
     * 根据证件号检测某个客户在某市场是否已存在
     * @param certificateNumber 证件号
     * @param marketId 市场ID
     * @return 如果客户在当前市场已存在，则返回错误(false)信息，如果不存在，则返回客户信息(若客户信息存在)
     */
    BaseOutput<Customer> checkExistByNoAndMarket(String certificateNumber,Long marketId);
}
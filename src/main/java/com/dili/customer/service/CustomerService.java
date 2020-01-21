package com.dili.customer.service;

import com.dili.customer.domain.Customer;
import com.dili.customer.domain.dto.CustomerBaseInfoDTO;
import com.dili.customer.domain.dto.CustomerCertificateInfoDTO;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

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
    BaseOutput saveBaseInfo(CustomerBaseInfoDTO baseInfo);

    /**
     * 保存客户证件相关新
     * @param certificateInfo
     * @return
     */
    BaseOutput saveCertificateInfo(CustomerCertificateInfoDTO certificateInfo);
}
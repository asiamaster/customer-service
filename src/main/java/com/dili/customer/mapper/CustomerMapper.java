package com.dili.customer.mapper;

import com.dili.customer.domain.Customer;
import com.dili.customer.domain.dto.CustomerQueryInput;
import com.dili.ss.base.MyMapper;

import java.util.List;

/**
 * @author yuehongbo
 */
public interface CustomerMapper extends MyMapper<Customer> {

    /**
     * 分页联合客户市场表查询客户信息
     * @param customerQueryInput
     * @return
     */
    List<Customer> listForPage(CustomerQueryInput customerQueryInput);
}
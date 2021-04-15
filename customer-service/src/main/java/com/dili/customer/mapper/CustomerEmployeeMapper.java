package com.dili.customer.mapper;

import com.dili.customer.domain.Customer;
import com.dili.customer.domain.CustomerEmployee;
import com.dili.customer.sdk.domain.dto.CustomerEmployeeDetailList;
import com.dili.customer.sdk.domain.dto.CustomerEmployeeList;
import com.dili.customer.sdk.domain.query.CustomerEmployeeDetailQuery;
import com.dili.customer.sdk.domain.query.CustomerEmployeeQuery;
import com.dili.ss.base.MyMapper;

import java.util.List;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/2 16:53
 */
public interface CustomerEmployeeMapper extends MyMapper<CustomerEmployee> {

    /**
     * 根据员工ID查询员工归属的有效客户信息
     * @param employeeId 员工ID
     * @return
     */
    List<Customer> listCustomerByEmployeeId(Long employeeId);

    /**
     * 根据客户员工关联ID(主键)查询员工归属的有效客户信息
     * @param id
     * @return
     */
    List<Customer> listCustomerById(Long id);

    /**
     * 前端页面列表查询客户员工信息
     * @param customerEmployeeQuery 查询条件
     * @return
     */
    List<CustomerEmployeeList> listForPage(CustomerEmployeeQuery customerEmployeeQuery);


    /**
     * 页面列表查询客户员工详细信息
     * @param customerEmployeeDetailQuery 查询条件
     * @return
     */
    List<CustomerEmployeeDetailList> listEmployeePage(CustomerEmployeeDetailQuery customerEmployeeDetailQuery);

}

package com.dili.customer.mapper;

import com.dili.customer.domain.Customer;
import com.dili.customer.domain.CustomerEmployee;
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

}

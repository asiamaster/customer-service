package com.dili.customer.service;

import com.dili.customer.domain.Customer;
import com.dili.customer.domain.CustomerEmployee;
import com.dili.customer.sdk.domain.dto.*;
import com.dili.customer.sdk.domain.query.CustomerEmployeeDetailQuery;
import com.dili.customer.sdk.domain.query.CustomerEmployeeQuery;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;

import java.util.List;
import java.util.Optional;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/2 16:54
 */
public interface CustomerEmployeeService extends BaseService<CustomerEmployee, Long> {

    /**
     * 开园区卡，创建员工信息
     * @param input 创建员工的提交数据
     * @return
     */
    BaseOutput<String> createdByOpenCard(EmployeeOpenCardInput input);

    /**
     * 根据客户ID及员工ID查询正常的绑定关系
     * @param customerId 客户ID
     * @param employeeId 员工ID
     * @return
     */
    Optional<CustomerEmployee> queryByCustomerAndEmployee(Long customerId, Long employeeId);

    /**
     * 客户销卡，删除客户持卡关系，并根据是否还持有卡而注销客户员工关系
     * @param employeeCancelCardInput    员工销卡信息输入项
     * @return
     */
    Optional<String> cancelCard(EmployeeCancelCardInput employeeCancelCardInput);

    /**
     * 员工客户持卡人换卡信息
     * @param employeeChangeCardInput 员工换卡信息输入项
     * @return
     */
    Optional<String> changeCard(EmployeeChangeCardInput employeeChangeCardInput);

    /**
     * 根据员工ID查询员工所属客户
     * @param employeeId 员工ID
     * @return
     */
    List<Customer> listCustomerByEmployeeId(Long employeeId);

    /**
     * 根据客户关联员工ID(主键)查询员工所属客户
     * @param id 员工ID
     * @return
     */
    List<Customer> listCustomerById(Long id);

    /**
     * 查询该市场的客户对应的员工信息
     * @param query
     * @return
     */
    PageOutput<List<CustomerEmployeeList>>  listPage(CustomerEmployeeQuery query);

    /**
     * 页面列表查询客户员工详细信息
     * @param query 查询条件
     * @return
     */
    PageOutput<List<CustomerEmployeeDetailList>> listEmployeePage(CustomerEmployeeDetailQuery query);


}

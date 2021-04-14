package com.dili.customer.service;

import com.dili.customer.domain.Customer;
import com.dili.customer.domain.Employee;
import com.dili.customer.domain.dto.EmployeeLoginDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;

import java.util.List;
import java.util.Optional;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/2 16:37
 */
public interface EmployeeService extends BaseService<Employee, Long> {

    /**
     * 根据手机号获取员工信息
     * @param cellphone
     * @return
     */
    Optional<Employee> getByCellphone(String cellphone);

    /**
     * 根据园区卡号及所属客户获取员工信息
     * @param cardNo     卡号
     * @param customerId 客户ID
     * @return
     */
    Optional<Employee> queryByCardNoAndCustomerId(String cardNo, Long customerId);

    /**
     * 保存员工基本信息，如果手机号重复，则返回已存在的数据，否则返回新增数据信息
     * @param employee 员工信息
     * @return
     */
    Employee add(Employee employee);

    /**
     * 员工登录
     * @param employeeLoginDto 员工登录
     * @return
     */
    BaseOutput login(EmployeeLoginDto employeeLoginDto);

    /**
     * 根据登录账户获取该员工对应的客户信息
     * @param userName 员工账号(手机号或者园区卡号)
     */
    List<Customer> listCustomerByUserName(String userName);

    /**
     * 重置员工登录密码
     * @param id
     */
    void resetPassword(Long id);

    /**
     * 根据员工ID更新登录密码
     * @param id 登录员工的ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否更新成功
     */
    BaseOutput<Boolean> changePassword(Long id,String oldPassword, String newPassword);
}

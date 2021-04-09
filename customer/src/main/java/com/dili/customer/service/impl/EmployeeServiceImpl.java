package com.dili.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.constants.CustomerServiceConstant;
import com.dili.customer.domain.Customer;
import com.dili.customer.domain.CustomerEmployee;
import com.dili.customer.domain.Employee;
import com.dili.customer.domain.EmployeeCard;
import com.dili.customer.domain.dto.EmployeeLoginDto;
import com.dili.customer.mapper.EmployeeMapper;
import com.dili.customer.service.CustomerEmployeeService;
import com.dili.customer.service.EmployeeCardService;
import com.dili.customer.service.EmployeeService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/2 16:38
 */
@Service
public class EmployeeServiceImpl extends BaseServiceImpl<Employee, Long> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private EmployeeCardService employeeCardService;
    @Autowired
    private CustomerEmployeeService customerEmployeeService;

    @Override
    public Optional<Employee> getByCellphone(String cellphone) {
        if (StrUtil.isNotBlank(cellphone)) {
            Employee condition = new Employee();
            condition.setCellphone(cellphone);
            Employee employee = employeeMapper.selectOne(condition);
            return Optional.ofNullable(employee);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Employee> queryByCardNoAndCustomerId(String cardNo, Long customerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", customerId);
        params.put("cardNo", cardNo);
        List<Employee> employeeList = employeeMapper.queryByCardNoAndCustomerId(params);
        return employeeList.stream().findFirst();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Employee add(Employee employee) {
        try {
            setPassword(employee);
            employee.setCreateTime(LocalDateTime.now());
            employee.setModifyTime(LocalDateTime.now());
            this.saveOrUpdateSelective(employee);
            return employee;
        } catch (Exception e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                Optional<Employee> byCellphoneTemp = this.getByCellphone(employee.getCellphone());
                return byCellphoneTemp.get();
            } else {
                throw e;
            }
        }
    }

    @Override
    public BaseOutput login(EmployeeLoginDto employeeLoginDto) {
        Employee employee = null;
        if (PhoneUtil.isMobile(employeeLoginDto.getUserName())) {
            Optional<Employee> byCellphone = this.getByCellphone(employeeLoginDto.getUserName());
            if (byCellphone.isPresent()) {
                employee = byCellphone.get();
                Optional<CustomerEmployee> customerEmployee = customerEmployeeService.queryByCustomerAndEmployee(employeeLoginDto.getCustomerId(), employee.getId());
                if (customerEmployee.isEmpty()) {
                    return BaseOutput.failure("企业员工关系已不存在").setCode(ResultCode.DATA_ERROR);
                }
            }
        } else {
            List<EmployeeCard> employeeCardList = employeeCardService.listByCardNo(employeeLoginDto.getUserName());
            if (CollectionUtil.isEmpty(employeeCardList)) {
                return BaseOutput.failure("账号或密码不正确").setMetadata("对应卡号不存在");
            }
            EmployeeCard employeeCard = employeeCardList.get(0);
            CustomerEmployee customerEmployee = customerEmployeeService.get(employeeCard.getCustomerEmployeeId());
            if (Objects.isNull(customerEmployee) || YesOrNoEnum.YES.getCode().equals(customerEmployee.getDeleted()) || !customerEmployee.getCustomerId().equals(employeeLoginDto.getCustomerId())) {
                return BaseOutput.failure("企业员工关系已不存在").setCode(ResultCode.DATA_ERROR);
            }
            employee = this.get(customerEmployee.getEmployeeId());
        }
        if (Objects.isNull(employee)) {
            return BaseOutput.failure("账号或密码不正确");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(employeeLoginDto.getPassword(), employee.getPassword())) {
            return BaseOutput.failure("账号或密码不正确").setCode(ResultCode.UNAUTHORIZED);
        }
        employee.setPassword("");
        return BaseOutput.successData(employee);
    }

    @Override
    public List<Customer> listCustomerByUserName(String userName) {
        if (PhoneUtil.isMobile(userName)) {
            Optional<Employee> byCellphone = this.getByCellphone(userName);
            if (byCellphone.isPresent()) {
                Long employeeId = byCellphone.get().getId();
                return customerEmployeeService.listCustomerByEmployeeId(employeeId);
            }
        } else {
            List<EmployeeCard> employeeCardList = employeeCardService.listByCardNo(userName);
            if (CollectionUtil.isNotEmpty(employeeCardList)) {
                return customerEmployeeService.listCustomerById(employeeCardList.get(0).getCustomerEmployeeId());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void resetPassword(Long id) {
        if (Objects.nonNull(id)) {
            Employee employee = this.get(id);
            if (Objects.nonNull(employee)) {
                employee.setPassword("");
                setPassword(employee);
                this.update(employee);
            }
        }
    }

    /**
     * 根据对象数据，按照规则设置数据密码
     * @param saveData
     */
    private void setPassword(Employee saveData) {
        /**
         * 如果存在输入密码的情况，则直接加密输入的密码
         */
        if (StrUtil.isNotBlank(saveData.getPassword())) {
            saveData.setPassword(new BCryptPasswordEncoder().encode(saveData.getPassword()));
        } else {
            saveData.setPassword(new BCryptPasswordEncoder().encode(CustomerServiceConstant.DEFAULT_PASSWORD));
        }
        saveData.setChangedPwdTime(LocalDateTime.now());
    }
}

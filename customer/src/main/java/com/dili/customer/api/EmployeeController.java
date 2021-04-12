package com.dili.customer.api;

import cn.hutool.json.JSONUtil;
import com.dili.customer.domain.Customer;
import com.dili.customer.domain.dto.EmployeeLoginDto;
import com.dili.customer.service.EmployeeService;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工基础信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/2 16:48
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * 员工根据手机号或者园区卡号登录
     * @param employeeLoginDto 登录对象
     * @return
     */
    @PostMapping("/login")
    public BaseOutput login(@Validated @RequestBody EmployeeLoginDto employeeLoginDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String collect = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
            log.warn(String.format("根据条件【%s】登录失败:%s", JSONUtil.toJsonStr(employeeLoginDto), collect));
            return BaseOutput.failure(collect);
        }
        try {
            return employeeService.login(employeeLoginDto);
        } catch (Exception e) {
            log.error(String.format("根据条件【%s】登录异常:%s", JSONUtil.toJsonStr(employeeLoginDto), e.getMessage()), e);
            return BaseOutput.failure("系统异常");
        }
    }

    /**
     * 根据登录账户获取该员工对应的客户信息
     * @param userName 员工账号(手机号或者园区卡号)
     */
    @PostMapping(value = "/listCustomerByUserName")
    public BaseOutput<List<Customer>> listCustomerByUserName(@RequestParam("userName") String userName) {
        try {
            return BaseOutput.successData(employeeService.listCustomerByUserName(userName));
        } catch (Exception e) {
            log.error(String.format("根据账号【%s】查询所属客户异常:%s", userName, e.getMessage()), e);
            return BaseOutput.failure("系统异常");
        }
    }

    /**
     * 根据id重置员工的密码
     * @param id
     * @return
     */
    @PostMapping(value = "/resetPassword")
    public BaseOutput resetPassword(@RequestParam("id") Long id){
        employeeService.resetPassword(id);
        return BaseOutput.success();
    }

}

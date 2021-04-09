package com.dili.customer.api;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.json.JSONUtil;
import com.dili.customer.sdk.domain.dto.EmployeeCancelCardInput;
import com.dili.customer.sdk.domain.dto.EmployeeChangeCardInput;
import com.dili.customer.sdk.domain.dto.EmployeeOpenCardInput;
import com.dili.customer.service.CustomerEmployeeService;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 客户员工信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/2 16:59
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customerEmployee")
public class CustomerEmployeeController {

    private final CustomerEmployeeService customerEmployeeService;

    /**
     * 开园区卡时，创建员工信息
     * @param input 员工信息输入项
     * @return
     */
    @PostMapping("/createdByOpenCard")
    public BaseOutput createdByOpenCard(@Validated @RequestBody EmployeeOpenCardInput input, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String collect = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
            log.warn(String.format("开卡创建员工数据【%s】验证失败:%s", JSONUtil.toJsonStr(input), collect));
            return BaseOutput.failure(collect);
        }
        try {
            return customerEmployeeService.createdByOpenCard(input);
        } catch (Exception e) {
            log.error(String.format("根据开卡条件【%s】创建员工操作异常:%s", JSONUtil.toJsonStr(input), e.getMessage()), e);
            return BaseOutput.failure("系统处理异常");
        }
    }

    /**
     * 员工销卡时，解除员工关系
     * @param employeeCancelCardInput 员工销卡信息输入项
     * @return
     */
    @PostMapping("/cancelCard")
    public BaseOutput cancelCard(@Validated @RequestBody EmployeeCancelCardInput employeeCancelCardInput, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String collect = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
            log.warn(String.format("根据条件【%s】销卡失败:%s", JSONUtil.toJsonStr(employeeCancelCardInput), collect));
            return BaseOutput.failure(collect);
        }
        try {
            Optional<String> s = customerEmployeeService.cancelCard(employeeCancelCardInput);
            if (s.isEmpty()) {
                log.info(String.format("根据条件【%s】销卡操作成功", JSONUtil.toJsonStr(employeeCancelCardInput)));
                return BaseOutput.success();
            }
            log.warn(String.format("根据条件【%s】销卡操作失败:%s", JSONUtil.toJsonStr(employeeCancelCardInput), s.get()));
            return BaseOutput.failure(s.get());
        } catch (Exception e) {
            log.error(String.format("根据条件【%s】销卡操作异常:%s", JSONUtil.toJsonStr(employeeCancelCardInput), e.getMessage()), e);
            return BaseOutput.failure("系统处理异常");
        }
    }

    /**
     * 员工换卡时，更新卡信息
     * @param employeeChangeCardInput 员工换卡信息输入项
     * @return
     */
    @PostMapping("/changeCard")
    public BaseOutput changeCard(@Validated @RequestBody EmployeeChangeCardInput employeeChangeCardInput, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String collect = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
            log.warn(String.format("根据条件【%s】换卡操作失败:%s", JSONUtil.toJsonStr(employeeChangeCardInput), collect));
            return BaseOutput.failure(collect);
        }
        try {
            Optional<String> s = customerEmployeeService.changeCard(employeeChangeCardInput);
            if (s.isEmpty()) {
                log.info(String.format("根据条件【%s】换卡操作成功", JSONUtil.toJsonStr(employeeChangeCardInput)));
                return BaseOutput.success();
            }
            log.warn(String.format("根据条件【%s】换卡操作失败:%s", JSONUtil.toJsonStr(employeeChangeCardInput), s.get()));
            return BaseOutput.failure(s.get());
        } catch (Exception e) {
            log.error(String.format("根据条件【%s】换卡操作异常:%s", JSONUtil.toJsonStr(employeeChangeCardInput), e.getMessage()), e);
            return BaseOutput.failure("系统处理异常");
        }
    }
}

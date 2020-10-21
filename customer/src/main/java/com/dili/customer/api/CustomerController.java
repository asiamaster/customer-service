package com.dili.customer.api;

import cn.hutool.core.util.StrUtil;
import com.dili.customer.domain.Customer;
import com.dili.customer.sdk.domain.dto.*;
import com.dili.customer.sdk.validator.AddView;
import com.dili.customer.sdk.validator.EnterpriseView;
import com.dili.customer.service.CustomerService;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 客户基础信息
 * This file was generated on 2019-12-27 14:43:13.
 * @author yuehongbo
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customer")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    /**
     * 分页查询客户数据集
     * @param customer
     * @return
     */
    @PostMapping(value="/listPage")
    public PageOutput<List<Customer>> listPage(@RequestBody CustomerQueryInput customer){
        if (Objects.isNull(customer.getMarketId())) {
            return PageOutput.failure("客户所属市场不能为空");
        }
        return customerService.listForPage(customer);
    }

    /**
     * 分页查询正常的客户数据集
     * 用户未删除切状态为生效的
     * @param customer
     * @return
     */
    @PostMapping(value="/listNormalPage")
    public PageOutput<List<Customer>> listNormalPage(@RequestBody CustomerQueryInput customer){
        if (Objects.isNull(customer.getMarketId())) {
            return PageOutput.failure("客户所属市场不能为空");
        }
        if (Objects.isNull(customer.getState())) {
            customer.setState(1);
        }
        customer.setIsDelete(0);
        return customerService.listForPage(customer);
    }

    /**
     * 只查询并返回客户基础数据，不会带有客户市场属性数据
     * 也不会查询 主表以外的数据
     * @param customer 客户查询条件
     * @return
     */
    @PostMapping(value = "/listBase")
    public BaseOutput<List<Customer>> listBase(@RequestBody CustomerBaseQueryInput customer) {
        PageOutput<List<Customer>> pageOutput = customerService.listBasePage(customer);
        return BaseOutput.success().setData(pageOutput.getData());
    }

    /**
     * 根据id及市场，查询客户的信息
     * @param id 客户ID
     * @param marketId 市场ID
     * @return
     */
    @PostMapping(value="/get")
    public BaseOutput<CustomerExtendDto> get(@RequestParam("id") Long id, @RequestParam("marketId") Long marketId){
        if (Objects.isNull(id) || Objects.isNull(marketId)) {
            return BaseOutput.failure("必要参数丢失").setCode(ResultCode.PARAMS_ERROR);
        }
        CustomerQueryInput condition = new CustomerQueryInput();
        condition.setId(id);
        condition.setMarketId(marketId);
        PageOutput<List<Customer>> pageOutput = customerService.listForPage(condition);
        Customer customer = pageOutput.getData().stream().findFirst().orElse(null);
        return BaseOutput.success().setData(customer);
    }

    /**
     * 根据id查询客户基本信息，不带有任何市场属性数据
     * @param id 客户ID
     * @return
     */
    @PostMapping(value = "/getById")
    public BaseOutput<Customer> getById(@RequestParam("id") Long id) {
        if (Objects.isNull(id)) {
            return BaseOutput.failure("必要参数丢失").setCode(ResultCode.PARAMS_ERROR);
        }
        return BaseOutput.success().setData(customerService.get(id));
    }

    /**
     * 根据证件号及市场，查询客户的信息
     * @param certificateNumber 客户证件号
     * @param marketId 市场ID
     * @return
     */
    @PostMapping(value="/getByCertificateNumber")
    public BaseOutput<Customer> getByCertificateNumber(@RequestParam("certificateNumber") String certificateNumber, @RequestParam("marketId") Long marketId){
        if (StrUtil.isBlank(certificateNumber) || Objects.isNull(marketId)) {
            return BaseOutput.failure("必要参数丢失").setCode(ResultCode.PARAMS_ERROR);
        }
        CustomerQueryInput condition = new CustomerQueryInput();
        condition.setCertificateNumber(certificateNumber);
        condition.setMarketId(marketId);
        PageOutput<List<Customer>> pageOutput = customerService.listForPage(condition);
        Customer customer = pageOutput.getData().stream().findFirst().orElse(null);
        return BaseOutput.success().setData(customer);
    }

    /**
     * 更新用户状态
     * @param customerId 客户ID
     * @param state      状态值
     * @return
     */
    @PostMapping(value = "/updateState")
    public BaseOutput<Customer> updateState(@RequestParam("customerId") Long customerId, @RequestParam("state") Integer state) {
        Customer data = customerService.get(customerId);
        data.setId(customerId);
        data.setState(state);
        customerService.updateSelective(data);
        return BaseOutput.success().setData(data);
    }

    /**
     * 查询客户数据集
     * @param customer
     * @return
     */
    @PostMapping(value="/list")
    public BaseOutput<List<Customer>> list(@RequestBody(required = false) CustomerQueryInput customer) {
        if (Objects.isNull(customer.getMarketId())) {
            return BaseOutput.failure("客户所属市场不能为空");
        }
        PageOutput<List<Customer>> pageOutput = customerService.listForPage(customer);
        return BaseOutput.success().setData(pageOutput.getData());
    }

    /**
     * 客户信息更新
     * @param updateInput 更新数据
     * @return BaseOutput
     */
    @PostMapping(value="/update")
    public BaseOutput<Customer> update(@Validated @RequestBody CustomerUpdateInput updateInput, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return customerService.update(updateInput);
    }

    /**
     * 企业客户注册
     * @param customer
     * @return BaseOutput
     */
    @PostMapping(value="/registerEnterprise")
    public BaseOutput<Customer> registerEnterprise(@Validated({AddView.class, EnterpriseView.class}) @RequestBody EnterpriseCustomerInput customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return customerService.register(customer);
    }

    /**
     * 个体户客户注册
     * @param customer
     * @return BaseOutput
     */
    @PostMapping(value="/registerIndividual")
    public BaseOutput<Customer> registerIndividual(@Validated({AddView.class}) @RequestBody IndividualCustomerInput customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        EnterpriseCustomerInput input = new EnterpriseCustomerInput();
        BeanUtils.copyProperties(customer,input);
        return customerService.register(input);
    }


    /**
     * 根据证件号检测某个客户在某市场是否已存在
     * @param certificateNumber 客户证件号
     * @param marketId 市场ID
     * @return 如果客户在当前市场已存在，则返回错误(false)信息，如果不存在，则返回客户信息(若客户信息存在)
     */
    @PostMapping(value="/checkExistByNoAndMarket")
    public BaseOutput<Customer> checkExistByNoAndMarket(@RequestParam(value = "certificateNumber") String certificateNumber,@RequestParam(value = "marketId") Long marketId) {
        return customerService.checkExistByNoAndMarket(certificateNumber,marketId);
    }
}
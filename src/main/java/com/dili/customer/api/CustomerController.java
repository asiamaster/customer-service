package com.dili.customer.api;

import com.dili.customer.domain.Customer;
import com.dili.customer.domain.dto.CustomerCertificateInfoInput;
import com.dili.customer.domain.dto.CustomerQueryInput;
import com.dili.customer.domain.dto.EnterpriseCustomerInput;
import com.dili.customer.domain.dto.IndividualCustomerInput;
import com.dili.customer.service.CustomerService;
import com.dili.customer.validator.AddView;
import com.dili.customer.validator.EnterpriseView;
import com.dili.customer.validator.UpdateView;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-12-27 14:43:13.
 * @author yuehongbo
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    /**
     * 分页查询客户数据集
     * @param customer
     * @return
     */
    @RequestMapping(value="/listPage", method = {RequestMethod.POST})
    public PageOutput<List<Customer>> listPage(@RequestBody(required = false) CustomerQueryInput customer){
        return customerService.listForPage(customer);
    }

    /**
     * 查询客户数据集
     * @param customer
     * @return
     */
    @RequestMapping(value="/list", method = {RequestMethod.POST})
    public BaseOutput<List<Customer>> list(@RequestBody(required = false) CustomerQueryInput customer) {
        PageOutput pageOutput = customerService.listForPage(customer);
        return BaseOutput.success().setData(pageOutput.getData());
    }

    /**
     * 保存客户基本信息
     * @param customer
     * @return BaseOutput
     */
    @RequestMapping(value="/saveBaseInfo", method = {RequestMethod.POST})
    public BaseOutput saveBaseInfo(@Validated(UpdateView.class) EnterpriseCustomerInput customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return customerService.saveBaseInfo(customer);
    }

    /**
     * 企业客户注册
     * @param customer
     * @return BaseOutput
     */
    @RequestMapping(value="/registerEnterprise", method = {RequestMethod.POST})
    public BaseOutput registerEnterprise(@Validated({AddView.class, EnterpriseView.class}) @RequestBody EnterpriseCustomerInput customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return customerService.saveBaseInfo(customer);
    }

    /**
     * 个体户客户注册
     * @param customer
     * @return BaseOutput
     */
    @RequestMapping(value="/registerIndividual", method = {RequestMethod.POST})
    public BaseOutput registerIndividual(@Validated({AddView.class}) @RequestBody IndividualCustomerInput customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        EnterpriseCustomerInput input = new EnterpriseCustomerInput();
        BeanUtils.copyProperties(customer,input);
        return customerService.saveBaseInfo(input);
    }

    /**
     * 保存客户证件信息信息
     * @param certificateInfo
     * @return BaseOutput
     */
    @RequestMapping(value="/saveCertificateInfo", method = {RequestMethod.POST})
    public BaseOutput saveCertificateInfo(@Validated @RequestBody CustomerCertificateInfoInput certificateInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return customerService.saveCertificateInfo(certificateInfo);
    }

    /**
     * 根据证件号检测某个客户在某市场是否已存在
     * @param certificateNumber 客户证件号
     * @param marketId 市场ID
     * @return 如果客户在当前市场已存在，则返回错误(false)信息，如果不存在，则返回客户信息(若客户信息存在)
     */
    @RequestMapping(value="/checkExistByNoAndMarket", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseOutput<Customer> checkExistByNoAndMarket(@RequestParam(value = "certificateNumber") String certificateNumber,@RequestParam(value = "marketId") Long marketId) {
        return customerService.checkExistByNoAndMarket(certificateNumber,marketId);
    }
}
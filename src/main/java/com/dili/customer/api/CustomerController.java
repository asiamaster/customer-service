package com.dili.customer.api;

import com.dili.customer.domain.Customer;
import com.dili.customer.domain.dto.CustomerBaseInfoInput;
import com.dili.customer.domain.dto.CustomerCertificateInfoInput;
import com.dili.customer.domain.dto.CustomerQueryInput;
import com.dili.customer.service.CustomerService;
import com.dili.customer.validator.AddView;
import com.dili.customer.validator.EnterpriseView;
import com.dili.customer.validator.UpdateView;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.PageOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
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
     * 跳转到Customer页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "customer/index";
    }

    /**
     * 分页查询客户数据集
     * @param customer
     * @return
     */
    @RequestMapping
    public PageOutput<List<Customer>> listPage(@RequestBody(required = false) CustomerQueryInput customer){
        return customerService.listPageByExample(customer);
    }

    /**
     * 查询客户数据集
     * @param customer
     * @return
     */
    @RequestMapping
    public BaseOutput<List<Customer>> list(@RequestBody(required = false) CustomerQueryInput customer) {
        return BaseOutput.success().setData(customerService.listByExample(customer));
    }

    /**
     * 保存客户基本信息
     * @param customer
     * @return BaseOutput
     */
    @RequestMapping(value="/saveBaseInfo.action", method = {RequestMethod.POST})
    @ResponseBody
    public BaseOutput saveBaseInfo(@Validated(UpdateView.class) CustomerBaseInfoInput customer, BindingResult bindingResult) {
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
    @RequestMapping(value="/registerEnterprise.action", method = {RequestMethod.POST})
    @ResponseBody
    public BaseOutput registerEnterprise(@Validated({AddView.class, EnterpriseView.class}) CustomerBaseInfoInput customer, BindingResult bindingResult) {
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
    @RequestMapping(value="/registerIndividual.action", method = {RequestMethod.POST})
    @ResponseBody
    public BaseOutput registerIndividual(@Validated({AddView.class}) CustomerBaseInfoInput customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return customerService.saveBaseInfo(customer);
    }

    /**
     * 保存客户证件信息信息
     * @param certificateInfo
     * @return BaseOutput
     */
    @RequestMapping(value="/saveCertificateInfo.action", method = {RequestMethod.POST})
    @ResponseBody
    public BaseOutput saveCertificateInfo(@Validated CustomerCertificateInfoInput certificateInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return customerService.saveCertificateInfo(certificateInfo);
    }

    /**
     * 保存客户证件信息信息
     * @param id 客户ID
     * @param firmId 市场ID
     * @return BaseOutput
     */
    @RequestMapping(value="/getAllInfoById.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Customer getAllInfoById(@RequestParam(value = "id") Long id,@RequestParam(value = "firmId") String firmId) {
        return null;
    }
}
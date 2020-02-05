package com.dili.customer.api;

import com.dili.customer.domain.Address;
import com.dili.customer.service.AddressService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-01-02 16:19:35.
 */
@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    /**
     * 保存客户地址信息
     * @param customerAddress 地址信息信息
     * @return BaseOutput
     */
    @RequestMapping(value = "/saveAddress.action", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseOutput saveAddress(@Validated @RequestBody Address customerAddress) {
        addressService.saveOrUpdate(customerAddress);
        return BaseOutput.success();
    }

    /**
     * 根据客户ID查询该客户的联系人信息
     * @param customerId 客户ID
     * @return
     */
    @RequestMapping(value="/listAllAddress.action", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseOutput<List<Address>> listAllAddress(@RequestParam("customerId") Long customerId) {
        Address condition = new Address();
        condition.setCustomerId(customerId);
        return BaseOutput.success().setData(addressService.list(condition));
    }
}
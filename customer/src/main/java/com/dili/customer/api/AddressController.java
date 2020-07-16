package com.dili.customer.api;

import com.dili.customer.domain.Address;
import com.dili.customer.service.AddressService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 客户地址信息
 * This file was generated on 2020-01-02 16:19:35.
 * @author yuehongbo
 */
@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * 根据客户ID查询该客户的联系地址信息
     * @param customerId 客户ID
     * @param marketId 所属市场
     * @return
     */
    @RequestMapping(value = "/listAllAddress", method = {RequestMethod.POST})
    public BaseOutput<List<Address>> listAllAddress(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId) {
        Address condition = new Address();
        condition.setCustomerId(customerId);
        condition.setMarketId(marketId);
        return BaseOutput.success().setData(addressService.list(condition));
    }
}
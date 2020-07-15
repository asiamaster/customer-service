package com.dili.customer.api;

import com.dili.customer.domain.CustomerMarket;
import com.dili.customer.service.CustomerMarketService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户所属市场
 * This file was generated on 2019-12-31 10:19:41.
 * @author yuehongbo
 */
@RestController
@RequestMapping("/api/customerMarket")
public class CustomerMarketController {

    @Autowired
    private CustomerMarketService customerMarketService;

    /**
     * 获取客户在某个市场的信息
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @return
     */
    @RequestMapping("/getByCustomerAndMarket")
    public BaseOutput<CustomerMarket> getByCustomerAndMarket(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId){
        return BaseOutput.success().setData(customerMarketService.queryByMarketAndCustomerId(marketId,customerId));
    }

}
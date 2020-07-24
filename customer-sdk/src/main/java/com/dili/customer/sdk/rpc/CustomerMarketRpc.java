package com.dili.customer.sdk.rpc;

import com.dili.customer.sdk.domain.CustomerMarket;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <B></B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/6/18 15:59
 */
//@FeignClient(name = "customer-service", contextId = "customerMarketRpc", url = "http://127.0.0.1:8181")
@FeignClient(name = "customer-service", contextId = "customerMarketRpc", url = "${customerService.url:}")
public interface CustomerMarketRpc {

    /**
     * 获取客户在某市场内的信息
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @return
     */
    @RequestMapping(value = "api/customerMarket/getByCustomerAndMarket", method = RequestMethod.POST)
    BaseOutput<CustomerMarket> getByCustomerAndMarket(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId);
}

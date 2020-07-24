package com.dili.customer.sdk.rpc;

import com.dili.customer.sdk.domain.TallyingArea;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 客户货位服务
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/7/18 16:41
 */
//@FeignClient(name = "customer-service",contextId = "tallyingAreaRpc", url = "http://127.0.0.1:8181")
@FeignClient(name = "customer-service", contextId = "tallyingAreaRpc", url = "${customerService.url:}")
public interface TallyingAreaRpc {

    /**
     * 根据客户ID查询该客户的联系人信息
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @return
     */
    @RequestMapping(value = "/api/tallyingArea/listTallyingArea", method = RequestMethod.POST)
    BaseOutput<List<TallyingArea>> listTallyingArea(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId);
}

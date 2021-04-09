package com.dili.customer.sdk.rpc;

import com.dili.customer.sdk.domain.BusinessCategory;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 客户经营品类信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/9/1 11:34
 */
@FeignClient(name = "customer-service", contextId = "businessCategoryRpc", url = "${customerService.url:}")
public interface BusinessCategoryRpc {

    /**
     * 根据客户ID查询该客户的理货区信息
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @return
     */
    @PostMapping(value = "/api/businessCategory/list")
    BaseOutput<List<BusinessCategory>> list(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId);
}

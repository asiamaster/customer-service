package com.dili.customer.sdk.rpc;

import com.dili.customer.sdk.constants.SecurityConstant;
import com.dili.customer.sdk.domain.CustomerMarket;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <B></B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/6/18 15:59
 */
@FeignClient(name = "customer-service", contextId = "customerMarketRpc", url = "${customerService.url:}")
public interface CustomerMarketRpc {

    /**
     * 获取客户在某市场内的信息
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @return
     */
    @PostMapping(value = "/api/customerMarket/getByCustomerAndMarket")
    BaseOutput<CustomerMarket> getByCustomerAndMarket(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId);

    /**
     * 更改客户所在市场的客户等级
     * @param customerId 客户ID
     * @param marketId 所属市场ID
     * @param nextGrade 想要更新成的等级
     * @param uapToken uapToken
     */
    @PostMapping("/api/customerMarket/changeGrade")
    BaseOutput changeGrade(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId, @RequestParam("grade") Integer nextGrade, @RequestHeader(SecurityConstant.UAP_TOKEN_KEY) String uapToken);

    /**
     * 更新用户状态,将获取当前登录人所在的市场为客户所属市场
     * @param customerId 客户ID
     * @param state      状态值
     * @param uapToken uapToken
     * @return
     */
    @PostMapping(value = "/api/customerMarket/updateState")
    BaseOutput updateState(@RequestParam("customerId") Long customerId, @RequestParam("state") Integer state, @RequestHeader(SecurityConstant.UAP_TOKEN_KEY) String uapToken);
}

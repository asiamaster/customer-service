package com.dili.customer.sdk.rpc;

import com.dili.customer.sdk.domain.TallyingArea;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 客户货位服务
 *
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/7/18 16:41
 */
@FeignClient(name = "customer-service", contextId = "tallyingAreaRpc", url = "${customerService.url:}")
public interface TallyingAreaRpc {

    /**
     * 根据客户ID查询该客户的理货区信息
     *
     * @param customerId 客户ID
     * @param marketId   市场ID
     * @return
     */
    @PostMapping(value = "/api/tallyingArea/listTallyingArea")
    BaseOutput<List<TallyingArea>> listTallyingArea(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId);

    /**
     * 根据条件查询该客户的理货区信息
     *
     * @param tallyingArea 查询条件
     * @return
     */
    @PostMapping(value = "/api/tallyingArea/listByExample")
    BaseOutput<List<TallyingArea>> listByExample(TallyingArea tallyingArea);

    /**
     * 同步资产租赁信息
     *
     * @param list 同步数据
     * @return
     */
    @PostMapping(value = "/api/tallyingArea/syncAssetsLease")
    BaseOutput<Boolean> syncAssetsLease(List<TallyingArea> list);

    /**
     * 根据批量客户ID查询该客户的理货区
     *
     * @param customerIds 客户ID
     * @param marketId    所属市场
     * @return
     */
    @PostMapping(value = "/batchQuery")
    BaseOutput<Map<Long, List<TallyingArea>>> batchQuery(@RequestParam("customerIds") Set<Long> customerIds, @RequestParam("marketId") Long marketId);

}

package com.dili.customer.api;

import com.dili.customer.domain.TallyingArea;
import com.dili.customer.service.TallyingAreaService;
import com.dili.ss.domain.BaseOutput;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 客户理货区货位服务
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/7/18 16:43
 */
@RestController
@RequestMapping("/api/tallyingArea")
public class TallyingAreaController {

    @Autowired
    private TallyingAreaService tallyingAreaService;

    /**
     * 根据客户ID查询该客户的理货区
     * @param customerId 客户ID
     * @param marketId 所属市场
     * @return
     */
    @PostMapping(value = "/listTallyingArea")
    public BaseOutput<List<TallyingArea>> listTallyingArea(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId) {
        TallyingArea condition = new TallyingArea();
        condition.setCustomerId(customerId);
        condition.setMarketId(marketId);
        return BaseOutput.success().setData(tallyingAreaService.list(condition));
    }

    /**
     * 根据条件查询该客户的理货区
     * @param tallyingArea 查询条件
     * @return
     */
    @PostMapping(value = "/listByExample")
    public BaseOutput<List<TallyingArea>> listByExample(@RequestBody TallyingArea tallyingArea) {
        return BaseOutput.success().setData(tallyingAreaService.listByExample(tallyingArea));
    }

    /**
     * 同步资产租赁信息
     * @param list 需要同步的数据
     * @return
     */
    @PostMapping(value = "/syncAssetsLease")
    public BaseOutput<Boolean> syncAssetsLease(@RequestBody List<TallyingArea> list) {
        tallyingAreaService.syncAssetsLease(list);
        return BaseOutput.success();
    }

    /**
     * 根据批量客户ID查询该客户的理货区
     * @param customerIds 客户ID
     * @param marketId 所属市场
     * @return
     */
    @PostMapping(value = "/batchQuery")
    public BaseOutput<Map<Long, List<TallyingArea>>> batchQuery(@RequestParam("customerIds") Set<Long> customerIds, @RequestParam("marketId") Long marketId) {
        if (CollectionUtils.isNotEmpty(customerIds) && customerIds.size() > 50) {
            return BaseOutput.failure("单次传入的客户数量不得大于50条");
        }
        Map<Long, List<TallyingArea>> tallyingAreaMap = tallyingAreaService.batchQuery(customerIds, marketId);
        return BaseOutput.success().setData(tallyingAreaMap);
    }
}

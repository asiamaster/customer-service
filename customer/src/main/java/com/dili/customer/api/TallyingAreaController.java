package com.dili.customer.api;

import com.dili.customer.domain.TallyingArea;
import com.dili.customer.service.TallyingAreaService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     * 根据客户ID查询该客户的联系地址信息
     * @param customerId 客户ID
     * @param marketId 所属市场
     * @return
     */
    @RequestMapping(value = "/listTallyingArea", method = {RequestMethod.POST})
    public BaseOutput<List<TallyingArea>> listTallyingArea(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId) {
        TallyingArea condition = new TallyingArea();
        condition.setCustomerId(customerId);
        condition.setMarketId(marketId);
        return BaseOutput.success().setData(tallyingAreaService.list(condition));
    }
}

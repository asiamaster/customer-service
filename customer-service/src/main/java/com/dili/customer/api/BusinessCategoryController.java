package com.dili.customer.api;

import com.dili.customer.domain.BusinessCategory;
import com.dili.customer.service.BusinessCategoryService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/9/1 11:36
 */
@RestController
@RequestMapping("/api/businessCategory")
public class BusinessCategoryController {

    @Autowired
    private BusinessCategoryService businessCategoryService;

    /**
     * 根据客户ID查询该客户的经营品类
     * @param customerId 客户ID
     * @param marketId 所属市场
     * @return
     */
    @PostMapping(value = "/list")
    public BaseOutput<List<BusinessCategory>> list(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId) {
        BusinessCategory condition = new BusinessCategory();
        condition.setCustomerId(customerId);
        condition.setMarketId(marketId);
        return BaseOutput.success().setData(businessCategoryService.list(condition));
    }
}

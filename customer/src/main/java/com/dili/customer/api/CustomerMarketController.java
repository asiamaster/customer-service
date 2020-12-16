package com.dili.customer.api;

import com.dili.customer.domain.CustomerMarket;
import com.dili.customer.domain.dto.CustomerMarketDto;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.service.CustomerMarketService;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 客户所属市场
 * This file was generated on 2019-12-31 10:19:41.
 * @author yuehongbo
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customerMarket")
public class CustomerMarketController {

    private final CustomerMarketService customerMarketService;

    /**
     * 获取客户在某个市场的信息
     * @param customerId 客户ID
     * @param marketId 市场ID
     * @return
     */
    @PostMapping("/getByCustomerAndMarket")
    public BaseOutput<CustomerMarket> getByCustomerAndMarket(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId){
        return BaseOutput.success().setData(customerMarketService.queryByMarketAndCustomerId(marketId,customerId));
    }

    /**
     * 更改客户所在市场的客户等级
     * @param customerId 客户ID
     * @param marketId 所属市场ID
     * @param nextGrade 想要更新成的等级
     */
    @PostMapping("/changeGrade")
    public BaseOutput changeGrade(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId, @RequestParam("grade") Integer nextGrade) {
        try {
            CustomerEnum.Grade instance = CustomerEnum.Grade.getInstance(nextGrade);
            if (Objects.nonNull(instance)) {
                customerMarketService.changeGrade(customerId, marketId, instance);
                return BaseOutput.success();
            } else {
                return BaseOutput.failure("目标等级不明确").setCode(ResultCode.PARAMS_ERROR);
            }
        } catch (Exception e) {
            log.error(String.format("更改市场[%d]客户[%d]等级[%s]异常[%s]", marketId, customerId, nextGrade, e.getMessage()), e);
            return BaseOutput.failure();
        }
    }

    /**
     * 根据客户ID获取所在的市场信息
     * @param customerId 客户ID
     * @return
     */
    @PostMapping("/selectByCustomerId")
    public BaseOutput<List<CustomerMarketDto>> selectByCustomerId(@RequestParam("customerId") Long customerId) {
        return BaseOutput.success().setData(customerMarketService.selectByCustomerId(customerId));
    }

    /**
     * 根据市场ID统计市场客户的审核情况
     * @param marketId 市场ID
     * @return
     */
    @PostMapping("/statisticsApproval")
    public BaseOutput statisticsApproval(@RequestParam("marketId") Long marketId) {
        return BaseOutput.success().setData(customerMarketService.statisticsApproval(marketId));
    }
}
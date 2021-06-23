package com.dili.customer.api;

import cn.hutool.json.JSONUtil;
import com.dili.customer.annotation.UapToken;
import com.dili.customer.domain.Customer;
import com.dili.customer.domain.CustomerMarket;
import com.dili.customer.domain.dto.CustomerMarketCharacterTypeDto;
import com.dili.customer.domain.dto.CustomerMarketDto;
import com.dili.customer.domain.dto.UapUserTicket;
import com.dili.customer.domain.vo.CustomerMarketVehicleVo;
import com.dili.customer.sdk.domain.dto.MarketApprovalResultInput;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.service.CustomerManageService;
import com.dili.customer.service.CustomerMarketService;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.util.LoggerUtil;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 客户所属市场
 * This file was generated on 2019-12-31 10:19:41.
 * @author yuehongbo
 */
@Slf4j
@RestController
@RequestMapping("/api/customerMarket")
public class CustomerMarketController {

    @Autowired
    private CustomerMarketService customerMarketService;
    @Autowired
    private UapUserTicket uapUserTicket;
    @Autowired
    private CustomerManageService customerManageService;

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
     * 更改客户所在市场的客户等级,将通过当前登录人所在的市场为指定为客户所属市场
     * @param customerId 客户ID
     * @param nextGrade 想要更新成的等级
     */
    @UapToken
    @PostMapping("/changeGrade")
    @BusinessLogger(businessType = "customer", operationType = "edit", systemCode = "CUSTOMER")
    public BaseOutput changeGrade(@RequestParam("customerId") Long customerId, @RequestParam("grade") Integer nextGrade) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            CustomerEnum.Grade instance = CustomerEnum.Grade.getInstance(nextGrade);
            if (Objects.nonNull(instance)) {
                Boolean aBoolean = customerMarketService.changeGrade(customerId, userTicket.getFirmId(), instance);
                if (aBoolean) {
                    Customer customer = customerManageService.get(customerId);
                    LoggerUtil.buildBusinessLoggerContext(customer.getId(), customer.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), String.format("更改客户等级为:%s", instance.getValue()));
                }
                return BaseOutput.success();
            } else {
                log.warn(String.format("更改市场[%d]客户[%d]的目标等级[%s]不支持", userTicket.getFirmId(), customerId, nextGrade, nextGrade));
                return BaseOutput.failure("不支持的客户等级").setCode(ResultCode.PARAMS_ERROR);
            }
        } catch (Exception e) {
            log.error(String.format("更改市场[%d]客户[%d]等级[%s]异常[%s]", userTicket.getFirmId(), customerId, nextGrade, e.getMessage()), e);
            return BaseOutput.failure();
        }
    }

    /**
     * 更新用户状态,将通过当前登录人所在的市场为指定为客户所属市场
     * @param customerId 客户ID
     * @param state      状态值
     * @return
     */
    @UapToken
    @PostMapping(value = "/updateState")
    @BusinessLogger(businessType = "customer", operationType = "edit", systemCode = "CUSTOMER")
    public BaseOutput updateState(@RequestParam("customerId") Long customerId, @RequestParam("state") Integer state) {
        CustomerEnum.State instance = CustomerEnum.State.getInstance(state);
        if (Objects.isNull(instance)) {
            return BaseOutput.failure("目标状态不支持");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        try {
            Optional<String> s = customerMarketService.updateState(customerId, userTicket.getFirmId(), state);
            if (s.isPresent()) {
                return BaseOutput.failure(s.get());
            }
            Customer customer = customerManageService.get(customerId);

            LoggerUtil.buildBusinessLoggerContext(customer.getId(), customer.getCode(), userTicket.getId(), userTicket.getRealName(), userTicket.getFirmId(), String.format("更改客户状态为:%s", instance.getValue()));
            return BaseOutput.success();
        } catch (Exception e) {
            log.error(String.format("更新市场[%s] 客户[%d] 状态为[%s]时异常[%s]", userTicket.getFirmId(), customerId, state, e.getMessage()), e);
            return BaseOutput.failure("操作异常");
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

    /**
     * 客户审核结果处理
     * @param input 审核结果
     * @return
     */
    @PostMapping("/approval")
    public BaseOutput<Boolean> approval(@RequestBody MarketApprovalResultInput input, BindingResult bindingResult) {
        log.info(String.format("客户信息审核结果:%s", JSONUtil.toJsonStr(input)));
        if (bindingResult.hasErrors()) {
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Optional<String> approval = customerMarketService.approval(input);
            if (approval.isPresent()) {
                return BaseOutput.failure(approval.get()).setData(false);
            }
            return BaseOutput.successData(true);
        } catch (Exception e) {
            log.error(String.format("客户信息审核结果:%s 修改异常:%s", JSONUtil.toJsonStr(input), e.getMessage()), e);
            return BaseOutput.failure("系统异常").setData(false);
        }
    }

    /**
     * 获取市场和其他类型子角色分类信息的组合对象
     *
     * @param marketIds 市场id集合
     * @return
     */
    @PostMapping(value = "/listOtherCharacterType")
    public BaseOutput<List<CustomerMarketCharacterTypeDto>> listOtherCharacterType(@RequestBody List<Long> marketIds) {
        return BaseOutput.success().setData(customerMarketService.listOtherCharacterType(marketIds));
    }

    /**
     * 获取客户所在市场和车辆信息
     *
     * @param customerId 客户id
     * @return
     */
    @PostMapping(value = "/listCustomerMarketAndVehicleInfo")
    public BaseOutput<List<CustomerMarketVehicleVo>> listCustomerMarketAndVehicleInfo(@RequestParam("customerId") Long customerId) {
        return BaseOutput.success().setData(customerMarketService.listCustomerMarketAndVehicleInfo(customerId));
    }
}
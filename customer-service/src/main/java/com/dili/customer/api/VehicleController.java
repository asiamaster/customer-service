package com.dili.customer.api;

import com.dili.customer.annotation.UapToken;
import com.dili.customer.domain.VehicleInfo;
import com.dili.customer.service.VehicleInfoService;
import com.dili.ss.domain.BaseOutput;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 客户车辆信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/16 14:06
 */
@RestController
@RequestMapping("/api/vehicle")
@Slf4j
public class VehicleController {

    @Autowired
    private VehicleInfoService vehicleInfoService;

    /**
     * 根据客户ID查询该客户的车辆信息
     * @param customerId 客户ID
     * @param marketId 所属市场
     * @return 客户车辆信息数据
     */
    @PostMapping(value = "/listVehicle")
    public BaseOutput<List<VehicleInfo>> listVehicle(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId) {
        return BaseOutput.success().setData(vehicleInfoService.listByCustomerAndMarket(customerId, marketId));
    }

    /**
     * 绑定客户车辆信息
     * @param vehicleInfo 客户ID
     * @return 客户地址信息数据
     */
    @UapToken
    @PostMapping(value = "/bindingVehicle")
    public BaseOutput<Boolean> bindingVehicle(@Validated @RequestBody VehicleInfo vehicleInfo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Optional<String> s = vehicleInfoService.bindingVehicle(vehicleInfo);
        if (s.isEmpty()) {
            return BaseOutput.successData(true);
        }
        return BaseOutput.failure(s.get()).setData(false);
    }

    /**
     * 删除客户车辆信息
     * @param id 数据ID
     * @return
     */
    @UapToken
    @PostMapping(value = "/delete")
    public BaseOutput delete(@RequestParam("id") Long id) {
        vehicleInfoService.deleteWithLogger(id);
        return BaseOutput.success("删除成功");
    }

    /**
     * 根据批量客户ID查询该客户的车辆信息
     * @param customerIds 客户ID
     * @param marketId 所属市场
     * @return
     */
    @PostMapping(value = "/batchQuery")
    public BaseOutput<Map<Long, List<VehicleInfo>>> batchQuery(@RequestParam("customerIds") Set<Long> customerIds, @RequestParam("marketId") Long marketId) {
        if (CollectionUtils.isNotEmpty(customerIds) && customerIds.size() > 50) {
            return BaseOutput.failure("单次传入的客户数量不得大于50条");
        }
        Map<Long, List<VehicleInfo>> vehicleInfoMap = vehicleInfoService.batchQuery(customerIds, marketId);
        return BaseOutput.success().setData(vehicleInfoMap);
    }
}

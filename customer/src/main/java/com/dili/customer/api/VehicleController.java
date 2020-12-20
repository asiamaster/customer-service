package com.dili.customer.api;

import cn.hutool.core.util.StrUtil;
import com.dili.customer.domain.VehicleInfo;
import com.dili.customer.service.VehicleInfoService;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户车辆信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/16 14:06
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/vehicle")
@Slf4j
public class VehicleController {

    private final VehicleInfoService vehicleInfoService;

    /**
     * 根据客户ID查询该客户的车辆信息
     * @param customerId 客户ID
     * @param marketId 所属市场
     * @return 客户车辆信息数据
     */
    @PostMapping(value = "/listVehicle")
    public BaseOutput<List<VehicleInfo>> listVehicle(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId) {
        VehicleInfo condition = new VehicleInfo();
        condition.setCustomerId(customerId);
        condition.setMarketId(marketId);
        return BaseOutput.success().setData(vehicleInfoService.list(condition));
    }

    /**
     * 绑定客户车辆信息
     * @param vehicleInfo 客户ID
     * @return 客户地址信息数据
     */
    @PostMapping(value = "/bindingVehicle")
    public BaseOutput<Boolean> bindingVehicle(@Validated @RequestBody VehicleInfo vehicleInfo) {
        String s = vehicleInfoService.bindingVehicle(vehicleInfo);
        if (StrUtil.isNotBlank(s)) {
            return BaseOutput.successData(true);
        }
        return BaseOutput.failure(s).setData(false);
    }
}
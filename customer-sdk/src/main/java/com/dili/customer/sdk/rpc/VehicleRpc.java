package com.dili.customer.sdk.rpc;

import com.dili.customer.sdk.constants.SecurityConstant;
import com.dili.customer.sdk.domain.VehicleInfo;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/16 14:34
 */
@FeignClient(name = "customer-service", contextId = "vehicleRpc", url = "${customerManageService.url:}")
public interface VehicleRpc {

    /**
     * 根据客户ID查询该客户的车辆信息
     * @param customerId 客户ID
     * @param marketId 所属市场
     * @return 客户车辆信息数据
     */
    @PostMapping(value = "/api/vehicle/listVehicle")
    BaseOutput<List<VehicleInfo>> listVehicle(@RequestParam("customerId") Long customerId, @RequestParam("marketId") Long marketId);

    /**
     * 绑定客户车辆信息
     * @param vehicleInfo 客户ID
     * @return 客户地址信息数据
     */
    @PostMapping(value = "/bindingVehicle")
    BaseOutput<Boolean> bindingVehicle(VehicleInfo vehicleInfo, @RequestHeader(SecurityConstant.UAP_TOKEN_KEY) String uapToken);
}

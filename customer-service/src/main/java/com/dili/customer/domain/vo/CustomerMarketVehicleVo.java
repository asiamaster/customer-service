package com.dili.customer.domain.vo;

import com.dili.uap.sdk.domain.Firm;
import lombok.Data;

import java.util.List;

@Data
public class CustomerMarketVehicleVo {

    private Firm firm;

    private List<VehicleInfoVo> vehicleInfoVos;
}

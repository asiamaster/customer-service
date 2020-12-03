package com.dili.customer.domain.dto;

import com.dili.customer.domain.VehicleInfo;
import com.dili.ss.domain.annotation.Operator;
import lombok.Data;

import javax.persistence.Column;
import java.util.Set;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/11/29 11:22
 */
@Data
public class VehicleInfoDto extends VehicleInfo {

    @Column(name = "`id`")
    @Operator(Operator.NOT_IN)
    private Set<Long> idNotSet;
}

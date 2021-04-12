package com.dili.customer.sdk.domain.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/9 17:36
 */
@Getter
@Setter
public class CustomerEmployeeList extends CustomerSimpleExtendDto {

    /**
     * 员工总数
     */
    private Integer employeeNum;
}

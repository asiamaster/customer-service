package com.dili.customer.domain.dto;

import com.dili.customer.domain.Address;
import com.dili.ss.domain.annotation.Operator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import java.util.Set;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/7/21 14:48
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AddressDto extends Address {

    @Column(name = "`id`")
    @Operator(Operator.NOT_IN)
    private Set<Long> idNotSet;
}

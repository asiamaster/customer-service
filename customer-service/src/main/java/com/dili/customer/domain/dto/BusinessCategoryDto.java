package com.dili.customer.domain.dto;

import com.dili.customer.domain.BusinessCategory;
import com.dili.ss.domain.annotation.Operator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import java.util.Set;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/21 10:39
 */
@Getter
@Setter
@ToString(callSuper = true)
public class BusinessCategoryDto extends BusinessCategory {

    /**
     * 客户ID集
     */
    @Column(name = "customer_id")
    @Operator(Operator.IN)
    private Set<Long> customerIdSet;
}

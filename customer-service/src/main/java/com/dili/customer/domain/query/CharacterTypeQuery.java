package com.dili.customer.domain.query;

import com.dili.customer.domain.CharacterType;
import com.dili.ss.domain.annotation.Operator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import java.util.Set;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/3/4 8:42
 */
@Getter
@Setter
@ToString(callSuper = true)
public class CharacterTypeQuery extends CharacterType {

    /**
     * 客户数据群
     */
    @Column(name = "customer_id")
    @Operator(Operator.IN)
    private Set<Long> customerIdSet;

    /**
     * 客户数据群
     */
    @Column(name = "market_id")
    @Operator(Operator.IN)
    private Set<Long> marketIdSet;
}

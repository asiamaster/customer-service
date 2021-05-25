package com.dili.customer.domain.dto;

import com.dili.customer.domain.TallyingArea;
import com.dili.ss.domain.annotation.Operator;
import lombok.Data;

import javax.persistence.Column;
import java.util.Set;

@Data
public class TallyingAreaDto extends TallyingArea {

    @Column(name = "`id`")
    @Operator(Operator.NOT_IN)
    private Set<Long> idNotSet;

    @Column(name = "`customer_id`")
    @Operator(Operator.IN)
    private Set<Long> customerIdSet;
}

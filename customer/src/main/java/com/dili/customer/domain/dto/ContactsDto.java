package com.dili.customer.domain.dto;

import com.dili.customer.domain.Contacts;
import com.dili.ss.domain.annotation.Operator;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Set;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/7/18 19:17
 */
@Getter
@Setter
public class ContactsDto extends Contacts {

    @Column(name = "`id`")
    @Operator(Operator.NOT_IN)
    private Set<Long> idNotSet;
}

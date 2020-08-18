package com.dili.customer.domain.dto;

import com.dili.customer.domain.Attachment;
import com.dili.ss.domain.annotation.Operator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import java.util.Set;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/8/17 18:06
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AttachmentDto extends Attachment {

    /**
     * 客户ID集
     */
    @Column(name = "customer_id")
    @Operator(Operator.IN)
    private Set<Long> customerIdSet;

    /**
     * 客户ID集
     */
    @Column(name = "id")
    @Operator(Operator.IN)
    private Set<Long> idSet;

    /**
     * id 不在集合中的数据操作
     */
    @Column(name = "`id`")
    @Operator(Operator.NOT_IN)
    private Set<Long> idNotSet;

}

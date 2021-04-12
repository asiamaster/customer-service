package com.dili.customer.sdk.domain.query;

import com.dili.ss.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户员工详细信息查询
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/12 16:42
 */
@Getter
@Setter
public class CustomerEmployeeDetailQuery extends BaseDomain {

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 关键字查询
     */
    private String keywords;
}

package com.dili.customer.sdk.domain.dto;

import com.dili.commons.bstable.Tablepar;
import lombok.Data;

import java.util.Set;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/8 14:26
 */
@Data
public class UserAccountQuery extends Tablepar {

    /**
     * 客户所属市场
     */
    private Long marketId;

    /**
     * 用户手机号
     */
    private String cellphone;

    /**
     * 用户账号
     */
    private String accountCode;

    /**
     * 禁启用状态
     */
    private Integer isEnable;

    /**
     * 客户ID集合
     */
    private Set<Long> customerIdSet;
}

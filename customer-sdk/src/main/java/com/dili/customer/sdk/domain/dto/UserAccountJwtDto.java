package com.dili.customer.sdk.domain.dto;

import lombok.Data;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/8 16:10
 */
@Data
public class UserAccountJwtDto {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 账号名称
     */
    private String accountName;

    /**
     * 用户登录账号
     */
    private String accountCode;

    /**
     * 是否启用
     * {@link com.dili.commons.glossary.YesOrNoEnum}
     */
    private Integer isEnable;
}

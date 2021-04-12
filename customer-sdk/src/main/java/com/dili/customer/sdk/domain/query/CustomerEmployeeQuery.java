package com.dili.customer.sdk.domain.query;

import com.dili.ss.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/4/9 17:26
 */
@Getter
@Setter
public class CustomerEmployeeQuery extends BaseDomain {

    /**
     * 市场ID
     */
    private Long marketId;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 客户编码
     */
    private String customerCode;
    /**
     * 客户联系电话
     */
    private String customerCellPhone;
    /**
     * 客户身份
     */
    private String characterSubType;
    /**
     * 员工名称
     */
    private String employeeName;
    /**
     * 员工电话
     */
    private String employeeCellPhone;
}

package com.dili.customer.sdk.constants;

/**
 * 客户信息验证用到的常量信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/2/3 21:27
 */
public interface ValidatedConstant {

    /**
     * 客户名称
     */
    String CUSTOMER_NAME_VALID_REGEXP = "^[·（）()a-zA-Z\\u4e00-\\u9fa5]+$";
    /**
     * 客户证件号
     */
    String CUSTOMER_CERTIFICATE_NUMBER_VALID_REGEXP = "^[()a-z0-9A-Z\\u4e00-\\u9fa5]+$";

    /**
     * 移动电话
     */
    String CUSTOMER_CELLPHONE_VALID_REGEXP = "^(1[3456789]\\d{9})$";


}

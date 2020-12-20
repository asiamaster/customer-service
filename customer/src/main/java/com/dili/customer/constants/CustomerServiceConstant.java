package com.dili.customer.constants;


import com.dili.customer.commons.constants.CustomerConstant;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/10/16 16:14
 */
public interface CustomerServiceConstant extends CustomerConstant {

    /**
     * 客户账号的默认密码
     */
    String DEFAULT_PASSWORD = "123456";

    /**
     * 溯源-司机端小程序
     */
    String TRACE_DRIVER_APPLET = "TRACE#DRIVER";

    /**
     * 溯源-买卖家小程序
     */
    String TRACE_BUYER_SELLER_APPLET = "TRACE#BUYER_SELLER";
}

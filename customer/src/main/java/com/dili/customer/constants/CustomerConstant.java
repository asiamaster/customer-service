package com.dili.customer.constants;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/10/16 16:14
 */
public interface CustomerConstant {

    /**
     * redis key 前缀
     */
    String REDIS_KEY_PREFIX = "customer_service:";

    /**
     * 客户账号的默认密码
     */
    String DEFAULT_PASSWORD = "123456";
}

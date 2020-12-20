package com.dili.customer.commons.constants;

/**
 * 客户基础常量信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/9 15:04
 */
public interface CustomerConstant {

    /**
     * redis key 前缀
     */
    String REDIS_KEY_PREFIX = "customer:";

    /**
     * 通用验证消息场景code
     */
    String COMMON_SCENE_CODE = "authCode";

    /**
     * 注册验证码消息场景code
     */
    String REGISTER_SCENE_CODE = "registerAuthCode";

    /**
     * 本地缓存前缀
     */
    String CACHE_KEY = "customer_cache";
}

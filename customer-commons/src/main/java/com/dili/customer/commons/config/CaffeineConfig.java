package com.dili.customer.commons.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/19 17:10
 */
@Configuration
public class CaffeineConfig {

    /**
     * 本地缓存，默认10分钟
     */
    @Bean("caffeineTimedCache")
    public Cache<String, String> caffeineTimedCache() {
        return Caffeine.newBuilder().expireAfterWrite(10L, TimeUnit.MINUTES).build();
    }

    /**
     * 本地缓存,指定最大存储数
     */
    @Bean("caffeineMaxSizeCache")
    public Cache<String, String> caffeineMaxSizeCache() {
        return Caffeine.newBuilder().maximumSize(500).build();
    }

}

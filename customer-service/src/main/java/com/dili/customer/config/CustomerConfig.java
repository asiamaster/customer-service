package com.dili.customer.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/10/16 16:33
 */
@Configuration("customerConfig")
public class CustomerConfig {

    /**
     * 同一手机号允许注册的客户数上限
     */
    @Value("${dili.customer.phone.limit:10}")
    @Getter
    private Integer phoneLimit;
}

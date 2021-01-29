package com.dili.customer.config;

import com.google.common.collect.Maps;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/12 16:35
 */
@Configuration
public class AppletSecretConfig {

    @Value("#{${dili.wechat.applet}}")
    @Getter
    private Map<String, String> wechatAppletSecret = Maps.newHashMap();

}

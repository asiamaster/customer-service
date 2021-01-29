package com.dili.customer.config;

import com.dili.customer.domain.dto.UapUserTicket;
import com.dili.customer.domain.wechat.AppletRequestInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/12 14:08
 */
@Configuration
public class AppConfig {

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public AppletRequestInfo appletRequestInfo() {
        return new AppletRequestInfo();
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public UapUserTicket uapUserTicket() {
        return new UapUserTicket();
    }
}

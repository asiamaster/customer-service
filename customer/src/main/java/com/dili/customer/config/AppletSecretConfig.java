package com.dili.customer.config;

import cn.hutool.json.JSONUtil;
import com.dili.customer.domain.wechat.AppletSecret;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/12 16:35
 */
@Configuration
public class AppletSecretConfig {

    @Value("#{${dili.wechat.applet}}")
    private Map<String, Map<String, String>> wechatAppletSecret = Maps.newHashMap();

    @Getter
    private Map<String, AppletSecret> initAppIdSecretMaps = Maps.newHashMap();

    @PostConstruct
    public void init() {
        wechatAppletSecret.forEach((t, v) -> {
            initAppIdSecretMaps.put(t.toUpperCase(), JSONUtil.toBean(JSONUtil.toJsonStr(v), AppletSecret.class));
        });
    }

    @PreDestroy
    public void destroy() {
        initAppIdSecretMaps = null;
    }
}

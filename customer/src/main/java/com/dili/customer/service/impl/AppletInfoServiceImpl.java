package com.dili.customer.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.customer.commons.constants.CustomerConstant;
import com.dili.customer.domain.AppletInfo;
import com.dili.customer.enums.AppletTerminalType;
import com.dili.customer.mapper.AppletInfoMapper;
import com.dili.customer.service.AppletInfoService;
import com.dili.ss.base.BaseServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/29 10:52
 */
@Service
public class AppletInfoServiceImpl extends BaseServiceImpl<AppletInfo, Long> implements AppletInfoService {

    public AppletInfoMapper getActualMapper() {
        return (AppletInfoMapper)getDao();
    }

    @Resource(name = "caffeineMaxSizeCache")
    private Cache<String, String> caffeineMaxSizeCache;

    @Override
    public Optional<AppletInfo> getByAppletTypeAndAppId(AppletTerminalType appletTerminalType, String appId) {
        if (Objects.isNull(appletTerminalType) || StrUtil.isBlank(appId)) {
            return Optional.empty();
        }
        StringBuilder keyBuilder = new StringBuilder(CustomerConstant.CACHE_KEY).append("_appletInfo_").append(appletTerminalType.getCode()).append("_").append(appId);
        String str = caffeineMaxSizeCache.get(keyBuilder.toString(), t -> {
            AppletInfo query = new AppletInfo();
            query.setAppletType(appletTerminalType.getCode());
            query.setAppId(appId);
            Optional<AppletInfo> first = this.list(query).stream().findFirst();
            if (first.isPresent()) {
                return JSONObject.toJSONString(first.get());
            }
            return null;
        });
        if (StrUtil.isNotBlank(str)) {
            AppletInfo appletInfo = JSONObject.parseObject(str, AppletInfo.class);
            return Optional.ofNullable(appletInfo);
        }
        return Optional.empty();
    }
}

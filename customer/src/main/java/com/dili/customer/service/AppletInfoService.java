package com.dili.customer.service;

import com.dili.customer.domain.AppletInfo;
import com.dili.customer.enums.AppletTerminalType;
import com.dili.ss.base.BaseService;

import java.util.Optional;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/29 10:51
 */
public interface AppletInfoService extends BaseService<AppletInfo, Long> {

    /**
     * 根据小程序类型加appId查询小程序信息
     * @param appletTerminalType 小程序类型
     * @param appId              应用ID
     * @return
     */
    Optional<AppletInfo> getByAppletTypeAndAppId(AppletTerminalType appletTerminalType, String appId);
}

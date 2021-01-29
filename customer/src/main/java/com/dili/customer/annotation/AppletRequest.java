package com.dili.customer.annotation;

import com.dili.customer.enums.AppletTerminalType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 小程序请求
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/12 14:23
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AppletRequest {

    /**
     * 小程序类型(微信?支付宝)
     * {@link com.dili.customer.enums.AppletTerminalType}
     * @return
     */
    AppletTerminalType appletType();
}

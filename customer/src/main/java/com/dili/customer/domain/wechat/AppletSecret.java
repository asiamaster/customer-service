package com.dili.customer.domain.wechat;

import lombok.Getter;
import lombok.Setter;

/**
 * 小程序 安全码信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/12 16:54
 */
@Getter
@Setter
public class AppletSecret {

    /**
     * 小程序 AppId
     */
    private String appId;
    /**
     * 小程序 secret
     */
    private String secret;
}

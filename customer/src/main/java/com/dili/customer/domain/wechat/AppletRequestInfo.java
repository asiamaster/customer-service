package com.dili.customer.domain.wechat;

import com.dili.customer.domain.AppletInfo;
import lombok.Data;

/**
 * 小程序系统信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/12 14:11
 */
@Data
public class AppletRequestInfo {

    /**
     * 系统中的小程序信息
     */
    private AppletInfo appletInfo;
}

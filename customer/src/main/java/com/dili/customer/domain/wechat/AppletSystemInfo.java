package com.dili.customer.domain.wechat;

import lombok.Data;

import java.util.Locale;

/**
 * 小程序系统信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/12 14:11
 */
@Data
public class AppletSystemInfo {

    /**
     * 小程序来源系统
     */
    private String appletSystem;

    /**
     * 小程序来源类型，司机端，买卖家等
     */
    private String appletCode;

    /**
     * 获取下划线链接的字符串
     * @return
     */
    public String getJoint() {
        return (appletSystem + "#" + appletCode).toUpperCase();
    }

}

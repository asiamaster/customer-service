package com.dili.customer.enums;

import lombok.Getter;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/28 19:04
 */
public enum AppletTerminalType {

    /**
     * 微信
     */
    WE_CHAT(1, "微信"),
    ;
    @Getter
    private Integer code;
    @Getter
    private String value;

    AppletTerminalType(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    /**
     * 获取某个枚举值实例信息
     *
     * @param code
     * @return
     */
    public static AppletTerminalType getInstance(Integer code) {
        for (AppletTerminalType as : AppletTerminalType.values()) {
            if (as.getCode().equals(code)) {
                return as;
            }
        }
        return null;
    }

    /**
     * 对比枚举值是否相等
     *
     * @param code
     * @return
     */
    public Boolean equalsToCode(Integer code) {
        return this.getCode().equals(code);
    }
}

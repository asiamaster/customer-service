package com.dili.customer.enums;

import lombok.Getter;

/**
 * 用户账号相关枚举
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/28 19:09
 */
public interface UserAccountEnum {

    /**
     * 账号终端类型
     */
     enum AccountTerminalType {

        /**
         * 待审核
         */
        WE_CHAT(1, "微信"),
        ;
        @Getter
        private Integer code;
        @Getter
        private String value;

        AccountTerminalType(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        /**
         * 获取某个枚举值实例信息
         *
         * @param code
         * @return
         */
        public static AccountTerminalType getInstance(String code) {
            for (AccountTerminalType as : AccountTerminalType.values()) {
                if (as.getCode().equals(code)) {
                    return as;
                }
            }
            return null;
        }

        /**
         * 对比枚举值是否相等
         * @param code
         * @return
         */
        public Boolean equalsToCode(Integer code) {
            return this.getCode().equals(code);
        }
    }
}

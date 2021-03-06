package com.dili.customer.sdk.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Set;

/**
 * <B>客户信息相关的枚举定义</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/6/18 15:29
 */
public class CustomerEnum {

    /**
     * 客户状态枚举定义
     */
    public enum State{

        NORMAL(1, "生效"),
        DISABLED(2, "禁用"),
        ;
        private Integer code;
        private String value;

        public Integer getCode() {
            return code;
        }
        public String getValue() {
            return value;
        }

        State(Integer code, String value) {
            this.code = code;
            this.value = value;
        }


        /**
         * 获取某个枚举值实例信息
         * @param code
         * @return
         */
        public static State getInstance(Integer code){
            for (State state : State.values()) {
                if (state.getCode().equals(code)){
                    return state;
                }
            }
            return null;
        }
    }

    /**
     * 客户组织类型
     */
    public enum OrganizationType {
        INDIVIDUAL("individual", "个人", "PC", "individualCustomer"),
        ENTERPRISE("enterprise", "企业", "BC", "enterpriseCustomer"),
        ;

        @Getter
        private String code;
        @Getter
        private String value;
        /**
         * 客户编码规则前缀
         */
        @Getter
        private String prefix;
        /**
         * uid编码生成时的业务类型
         */
        @Getter
        private String uidType;

        OrganizationType(String code, String value, String prefix, String uidType) {
            this.code = code;
            this.value = value;
            this.prefix = prefix;
            this.uidType = uidType;
        }

        /**
         * 获取某个枚举值实例信息
         *
         * @param code
         * @return
         */
        public static OrganizationType getInstance(String code) {
            for (OrganizationType type : OrganizationType.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * 客户等级
     */
    public enum Grade {

        /**
         * 非会员客户
         */
        NONMEMBER(0, "非会员客户") {
            @Override
            public Set<Grade> next() {
                return Set.of(GENERAL);
            }
        },
        /**
         * 普通客户
         */
        GENERAL(1, "普通客户") {
            @Override
            public Set<Grade> next() {
                return Set.of(KAC);
            }
        },
        /**
         * 重点客户
         */
        KAC(2, "重点客户") {
            @Override
            public Set<Grade> next() {
                return Collections.emptySet();
            }
        },
        ;

        @Getter
        private Integer code;
        @Getter
        private String value;

        Grade(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        //当前状态可流转到的下一状态定义
        public abstract Set<Grade> next();

        /**
         * 获取某个枚举值实例信息
         * @param code
         * @return
         */
        public static Grade getInstance(Integer code) {
            for (Grade grade : Grade.values()) {
                if (grade.getCode().equals(code)) {
                    return grade;
                }
            }
            return null;
        }
    }

    /**
     * 客户性别枚举定义
     */
    public enum Gender{

        MALE(1, "男"),
        FEMALE(2, "女"),
        ;
        private Integer code;
        private String value;

        public Integer getCode() {
            return code;
        }
        public String getValue() {
            return value;
        }

        Gender(Integer code, String value) {
            this.code = code;
            this.value = value;
        }


        /**
         * 获取某个枚举值实例信息
         * @param code
         * @return
         */
        public static Gender getInstance(Integer code){
            for (Gender gender : Gender.values()) {
                if (gender.getCode().equals(code)){
                    return gender;
                }
            }
            return null;
        }
    }

    /**
     * 客户附件类别
     */
    public enum AttachmentType{

        /**
         * 营业执照
         */
        营业执照(1, "营业执照"),
        ;
        private Integer code;
        private String value;

        public Integer getCode() {
            return code;
        }
        public String getValue() {
            return value;
        }

        AttachmentType(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        /**
         * 获取某个枚举值实例信息
         * @param code
         * @return
         */
        public static AttachmentType getInstance(Integer code){
            for (AttachmentType at : AttachmentType.values()) {
                if (at.getCode().equals(code)){
                    return at;
                }
            }
            return null;
        }
    }
}

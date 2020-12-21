package com.dili.customer.sdk.enums;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
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
    public enum State {

        NORMAL(1, "正常"),
        DISABLED(2, "禁用"),
        USELESS(3, "未生效"),
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
         *
         * @param code
         * @return
         */
        public static State getInstance(Integer code) {
            for (State state : State.values()) {
                if (state.getCode().equals(code)) {
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
        INDIVIDUAL("individual", "个人"),
        ENTERPRISE("enterprise", "企业"),
        ;

        @Getter
        private String code;
        @Getter
        private String value;

        OrganizationType(String code, String value) {
            this.code = code;
            this.value = value;
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

        /**
         * 对比枚举值是否相等
         * @param code
         * @return
         */
        public Boolean equalsToCode(String code) {
            return this.getCode().equals(code);
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
         *
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
    public enum Gender {

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
         *
         * @param code
         * @return
         */
        public static Gender getInstance(Integer code) {
            for (Gender gender : Gender.values()) {
                if (gender.getCode().equals(code)) {
                    return gender;
                }
            }
            return null;
        }
    }

    /**
     * 客户附件类别
     */
    public enum AttachmentType {

        /**
         * 营业执照
         */
        营业执照(1, "营业执照") {
            @Override
            public Set<OrganizationType> useFor() {
                return Set.of(OrganizationType.INDIVIDUAL, OrganizationType.ENTERPRISE);
            }
        },
        /**
         * 食品经营许可证
         */
        食品经营许可证(2, "食品经营许可证") {
            @Override
            public Set<OrganizationType> useFor() {
                return Set.of(OrganizationType.INDIVIDUAL, OrganizationType.ENTERPRISE);
            }
        },
        /**
         * 生产许可证
         */
        生产许可证(3, "生产许可证") {
            @Override
            public Set<OrganizationType> useFor() {
                return Set.of(OrganizationType.INDIVIDUAL, OrganizationType.ENTERPRISE);
            }
        },
        /**
         * 法人证件
         */
        法人证件(4, "法人证件") {
            @Override
            public Set<OrganizationType> useFor() {
                return Set.of(OrganizationType.ENTERPRISE);
            }
        },
        /**
         * 个人证件
         */
        个人证件(5, "个人证件") {
            @Override
            public Set<OrganizationType> useFor() {
                return Set.of(OrganizationType.INDIVIDUAL);
            }
        },
        ;
        private Integer code;
        private String value;

        //当前状态可流转到的下一状态定义
        public abstract Set<OrganizationType> useFor();

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
         *
         * @param code
         * @return
         */
        public static AttachmentType getInstance(Integer code) {
            for (AttachmentType at : AttachmentType.values()) {
                if (at.getCode().equals(code)) {
                    return at;
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

        /**
         * 根据组织类型获取该类型对应的附件类型
         * @param organizationType
         * @return
         */
        public static List<AttachmentType> getByOrganizationType(OrganizationType organizationType) {
            List<AttachmentType> attachmentTypeList = Lists.newArrayList();
            for (AttachmentType at : AttachmentType.values()) {
                if (at.useFor().contains(organizationType)) {
                    attachmentTypeList.add(at);
                }
            }
            return attachmentTypeList;
        }
    }

    /**
     * 客户角色身份
     */
    public enum CharacterType {

        经营户("business_user_character_type", "经营户", Boolean.FALSE),
        买家("buyer_character_type", "买家", Boolean.FALSE),
        其他类型("other_character_type", "其他类型", Boolean.FALSE),
        ;
        @Getter
        private String code;
        @Getter
        private String value;
        /**
         * 子类是否多选
         */
        @Getter
        private Boolean multiple;

        CharacterType(String code, String value, Boolean multiple) {
            this.code = code;
            this.value = value;
            this.multiple = multiple;
        }

        /**
         * 获取某个枚举值实例信息
         * @param code
         * @return
         */
        public static CharacterType getInstance(String code) {
            for (CharacterType at : CharacterType.values()) {
                if (at.getCode().equals(code)) {
                    return at;
                }
            }
            return null;
        }

        /**
         * 获取某个枚举值实例信息
         * @param code
         * @return
         */
        public static String getValueByCode(String code) {
            for (CharacterType at : CharacterType.values()) {
                if (at.getCode().equals(code)) {
                    return at.value;
                }
            }
            return "";
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


    /**
     * 客户资料审核状态
     */
    public enum ApprovalStatus {

        /**
         * 待审核
         */
        WAIT_CONFIRM(1, "待审核"),
        /**
         * 已通过
         */
        PASSED(2, "已通过"),
        /**
         * 未通过
         */
        UN_PASS(3, "未通过"),
        ;
        @Getter
        private Integer code;
        @Getter
        private String value;

        ApprovalStatus(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        /**
         * 获取某个枚举值实例信息
         *
         * @param code
         * @return
         */
        public static ApprovalStatus getInstance(String code) {
            for (ApprovalStatus as : ApprovalStatus.values()) {
                if (as.getCode().equals(code)) {
                    return as;
                }
            }
            return null;
        }

        /**
         * 获取某个枚举值实例信息
         *
         * @param code
         * @return
         */
        public static String getValueByCode(Integer code) {
            for (ApprovalStatus as : ApprovalStatus.values()) {
                if (as.getCode().equals(code)) {
                    return as.value;
                }
            }
            return "";
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

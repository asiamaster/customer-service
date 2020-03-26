package com.dili.customer.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.domain.annotation.Like;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 由MyBatis Generator工具自动生成
 * 客户基础数据
企业客户没有性别和民族和certificate_time，但有certificate_rang
 * This file was generated on 2020-01-09 17:36:22.
 */
@Table(name = "`customer`")
@Getter
@Setter
public class Customer extends BaseDomain {
    @Id
    @Column(name = "`id`",updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户编号
     */
    @Column(name = "`code`",updatable = false)
    private String code;

    /**
     * 证件号
     */
    @Column(name = "`certificate_number`", updatable = false)
    private String certificateNumber;

    /**
     * 证件类型
     */
    @Column(name = "`certificate_type`")
    private String certificateType;

    /**
     * 证件日期##企业时为营业执照日期,如:2011-09-01 至 长期
     */
    @Column(name = "`certificate_range`")
    private String certificateRange;

    /**
     * 证件是否长期有效 1-是；0-否
     */
    @Column(name = "`certificate_long_term`")
    private Integer certificateLongTerm;

    /**
     * 证件地址
     */
    @Column(name = "`certificate_addr`")
    private String certificateAddr;

    /**
     * 客户名称
     */
    @Column(name = "`name`")
    @Like
    private String name;

    /**
     * 出生日期
     */
    @Column(name = "`birthdate`")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate birthdate;

    /**
     * 性别:男,女
     */
    @Column(name = "`gender`")
    private Integer gender;

    /**
     * 照片
     */
    @Column(name = "`photo`")
    private String photo;

    /**
     * 客户等级
     */
    @Column(name = "`grade`")
    private Integer grade;

    /**
     * 手机号
     */
    @Column(name = "`cellphone`")
    private String cellphone;

    /**
     * 联系电话
     */
    @Column(name = "`contacts_phone`")
    private String contactsPhone;

    /**
     * 联系人
     */
    @Column(name = "`contacts_name`")
    private String contactsName;


    /**
     * 组织类型,个人/企业
     */
    @Column(name = "`organization_type`")
    private String organizationType;

    /**
     * 来源系统##外部系统来源标识
     */
    @Column(name = "`source_system`", updatable = false)
    private String sourceSystem;

    /**
     * 来源渠道##租赁业务、系统创建等
     */
    @Column(name = "`source_channel`", updatable = false)
    private String sourceChannel;

    /**
     * 客户行业##水果批发/蔬菜批发/超市
     */
    @Column(name = "`profession`")
    private String profession;

    /**
     * 经营地区##经营地区城市id
     */
    @Column(name = "`operating_area`")
    private String operatingArea;

    /**
     * 经营地区经度
     */
    @Column(name = "`operating_lng`")
    private String operatingLng;

    /**
     * 经营地区纬度
     */
    @Column(name = "`operating_lat`")
    private String operatingLat;

    /**
     * 其它头衔
     */
    @Column(name = "`other_title`")
    private String otherTitle;

    /**
     * 主营品类
     */
    @Column(name = "`main_category`")
    private String mainCategory;

    /**
     * 注册资金##企业客户属性
     */
    @Column(name = "`registered_capital`")
    private Long registeredCapital;

    /**
     * 企业员工数
     */
    @Column(name = "`employee_number`")
    private String employeeNumber;

    /**
     * 证件类型
     */
    @Column(name = "`corporation_certificate_type`")
    private String corporationCertificateType;

    /**
     * 法人证件号
     */
    @Column(name = "`corporation_certificate_number`")
    private String corporationCertificateNumber;

    /**
     * 法人真实姓名
     */
    @Column(name = "`corporation_name`")
    private String corporationName;

    /**
     * 手机号是否验证
     */
    @Column(name = "`is_cellphone_valid`")
    private Integer isCellphoneValid;

    /**
     * 创建人
     */
    @Column(name = "`creator_id`",updatable = false)
    private Long creatorId;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`",updatable = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    /**
     * 是否删除
     */
    @Column(name = "`is_delete`")
    private Integer isDelete;

    /**
     * 客户状态 0注销，1生效，2禁用，
     */
    @Column(name = "`state`")
    private Integer state;

    /**
     * 客户所在市场的创建人
     * 数据来源于客户市场表的创建人
     */
    @Transient
    private Long marketCreatorId;

    /**
     * 客户所在市场的创建时间
     * 数据来源于客户市场表的创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Transient
    private LocalDateTime marketCreateTime;

    /**
     * 客户当前所属的市场
     * 数据来源于客户市场表的市场ID
     */
    @Transient
    private Long marketId;
}
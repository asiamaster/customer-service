package com.dili.customer.domain;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.dili.customer.utils.CustomerInfoUtil;
import com.dili.ss.dao.sql.DateNextVersion;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.domain.annotation.Like;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 客户基础数据
企业客户没有性别和民族和certificate_time，但有certificate_rang
 * This file was generated on 2020-01-09 17:36:22.
 */
@Table(name = "`customer`")
@Getter
@Setter
@ToString(callSuper = true)
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
    @NotBlank(message = "客户证件号不能为空")
    @Size(min = 1, max = 40, message = "证件号码请保持在40个字以内")
    private String certificateNumber;

    /**
     * 证件类型
     */
    @Column(name = "`certificate_type`")
    @NotBlank(message = "客户证件类型不能为空")
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
    @NotBlank(message = "客户名称不能为空")
    @Size(min = 1, max = 40, message = "客户名称请保持在40个字以内")
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
     * 联系电话
     */
    @Column(name = "`contacts_phone`")
    @NotBlank(message = "客户联系手机不能为空")
    @Pattern(regexp = "^(1[3456789]\\d{9})$", message = "客户联系方式格式不正确")
    private String contactsPhone;

    /**
     * 联系人
     */
    @Column(name = "`contacts_name`")
    @NotBlank(message = "客户联系人不能为空")
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
    @NotBlank(message = "客户来源系统不能为空")
    private String sourceSystem;

    /**
     * 来源渠道##租赁业务、系统创建等
     */
    @Column(name = "`source_channel`", updatable = false)
    @NotBlank(message = "客户来源渠道不能为空")
    private String sourceChannel;

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
     * 现住址城市ID path
     * 格式为 100000,110000,111100
     */
    @Column(name = "current_city_path")
    private String currentCityPath;

    /**
     * 现住址城市名称
     */
    @Column(name = "current_city_name")
    private String currentCityName;

    /**
     * 现住址详细地址
     */
    @Column(name = "current_address")
    private String currentAddress;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HHm:m:ss")
    @Version(nextVersion = DateNextVersion.class)
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
     * 客户市场服务信息
     * 客户市场本身是一对多的关系，而此处使用单个对象，是按一个客户一个市场信息的单一关系返回
     */
    @Transient
    private CustomerMarket customerMarket;

    /**
     * 客户理货区信息
     */
    @Transient
    private List<TallyingArea> tallyingAreaList;

    /**
     * 客户证件号打码加*显示
     */
    @Transient
    private String certificateNumberMask;

    public String getCertificateNumberMask() {
        if (StrUtil.isNotBlank(certificateNumber)) {
            return CustomerInfoUtil.certificateNumberHide(certificateNumber);
        }
        return certificateNumberMask;
    }

}
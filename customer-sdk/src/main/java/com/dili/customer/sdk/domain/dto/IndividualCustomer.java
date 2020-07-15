package com.dili.customer.sdk.domain.dto;


import com.dili.customer.sdk.validator.AddView;
import com.dili.customer.sdk.validator.UpdateView;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/4 9:53
 */
public class IndividualCustomer implements Serializable {
    private static final long serialVersionUID = -5865840494367827998L;

    /**
     * 客户ID
     */
    @NotNull(message = "业务关键ID不能为空",groups = {UpdateView.class})
    private Long id;

    /**
     * 证件号
     */
    @NotBlank(message = "证件号码不能为空", groups = {AddView.class})
    @Size(min = 1, max = 40, message = "证件号码请保持在40个字以内", groups = {AddView.class})
    private String certificateNumber;

    /**
     * 证件类型
     */
    @NotBlank(message = "证件类型不能为空", groups = {AddView.class})
    private String certificateType;

    /**
     * 客户名称
     */
    @NotBlank(message = "客户名称不能为空", groups = {AddView.class,UpdateView.class})
    @Size(min = 1, max = 40, message = "客户名称请保持在40个字以内", groups = {AddView.class,UpdateView.class})
    private String name;

    /**
     * 组织类型,个人/企业
     */
    @NotBlank(message = "组织类型不能为空", groups = {AddView.class})
    private String organizationType;

    /**
     * 来源系统##外部系统来源标识
     */
    @NotBlank(message = "客户来源系统不能为空", groups = {AddView.class})
    private String sourceSystem;

    /**
     * 来源渠道##摊位租赁、系统注册等
     */
    @NotBlank(message = "客户来源渠道不能为空", groups = {AddView.class})
    private String sourceChannel;


    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空", groups = {AddView.class,UpdateView.class})
    @Pattern(regexp = "^(1[3456789]\\d{9})$", message = "请输入正确的联系方式", groups = {AddView.class,UpdateView.class})
    private String contactsPhone;

    /**
     * 客户编码
     */
    private String code;

    /**
     * 客户等级
     */
    private Integer grade;

    /**
     * 创建人
     */
    private Long operatorId;

    /**
     * 客户归属人
     */
    private Long ownerId;

    /**
     * 客户所属市场
     */
    private Long marketId;

    /**
     * 客户状态
     */
    private Integer state;

    /**
     * 归属部门
     */
    private Long departmentId;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 照片
     */
    private String photo;

    /**
     * 出生日期
     */
    private LocalDate birthdate;

    /**
     * 证件地址
     */
    private String certificateAddr;

    /**
     * 证件日期##企业时为营业执照日期,如:2011-09-01 至 长期
     */
    private String certificateRange;

    /**
     * 证件是否长期有效 1-是；0-否
     */
    private Integer certificateLongTerm;

    /**
     * 客户身份类型(买家、卖家等)
     */
    private String customerType;

    /**
     * 现住址城市ID
     */
    private Long currentCityId;

    /**
     * 现住址城市名称
     */
    private String currentCityName;

    /**
     * 现住址详细地址
     */
    private String currentAddress;



    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCertificateNumber() {
        return certificateNumber;
    }
    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }
    public String getCertificateType() {
        return certificateType;
    }
    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOrganizationType() {
        return organizationType;
    }
    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }
    public String getSourceSystem() {
        return sourceSystem;
    }
    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }
    public String getSourceChannel() {
        return sourceChannel;
    }
    public void setSourceChannel(String sourceChannel) {
        this.sourceChannel = sourceChannel;
    }
    public String getContactsPhone() {
        return contactsPhone;
    }
    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Integer getGrade() {
        return grade;
    }
    public void setGrade(Integer grade) {
        this.grade = grade;
    }
    public Long getOperatorId() {
        return operatorId;
    }
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }
    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public Long getMarketId() {
        return marketId;
    }
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }
    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }
    public Long getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    public Integer getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
    public Integer getGender() {
        return gender;
    }
    public void setGender(Integer gender) {
        this.gender = gender;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public LocalDate getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
    public String getCertificateAddr() {
        return certificateAddr;
    }
    public void setCertificateAddr(String certificateAddr) {
        this.certificateAddr = certificateAddr;
    }
    public String getCertificateRange() {
        return certificateRange;
    }
    public void setCertificateRange(String certificateRange) {
        this.certificateRange = certificateRange;
    }
    public Integer getCertificateLongTerm() {
        return certificateLongTerm;
    }
    public void setCertificateLongTerm(Integer certificateLongTerm) {
        this.certificateLongTerm = certificateLongTerm;
    }
    public String getCustomerType() {
        return customerType;
    }
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
}

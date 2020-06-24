package com.dili.customer.sdk.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <B></B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/6/18 15:53
 */
public class CustomerMarket extends BaseDomain {

    /**
     * ID
     */
    private Long id;

    /**
     * 归属组织
     */
    private Long marketId;

    /**
     * 归属部门##内部创建归属到创建员工的部门
     */
    private Long departmentId;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 所有者
     */
    private Long ownerId;

    /**
     * 客户等级
     */
    private Integer grade;

    /**
     * 客户行业##水果批发/蔬菜批发/超市
     */
    private String profession;

    /**
     * 经营地区##经营地区城市id
     */
    private String operatingArea;

    /**
     * 经营地区经度
     */
    private String operatingLng;

    /**
     * 经营地区纬度
     */
    private String operatingLat;

    /**
     * 其它头衔
     */
    private String otherTitle;

    /**
     * 主营品类
     */
    private String mainCategory;

    /**
     * 客户别名
     */
    private String alias;

    /**
     * 客户类型##采购、销售、代买等##{provider:"dataDictionaryValueProvider",queryParams:{dd_id:4}}
     */
    private String type;

    /**
     * 备注信息
     */
    private String notes;

    /**
     * 在本市场的初始创建人
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改人id
     */
    private Long modifierId;

    /**
     * 修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;


    @Override
    public Long getId() {
        return id;
    }
    @Override
    public void setId(Long id) {
        this.id = id;
    }
    public Long getMarketId() {
        return marketId;
    }
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }
    public Long getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
    public Integer getGrade() {
        return grade;
    }
    public void setGrade(Integer grade) {
        this.grade = grade;
    }
    public String getProfession() {
        return profession;
    }
    public void setProfession(String profession) {
        this.profession = profession;
    }
    public String getOperatingArea() {
        return operatingArea;
    }
    public void setOperatingArea(String operatingArea) {
        this.operatingArea = operatingArea;
    }
    public String getOperatingLng() {
        return operatingLng;
    }
    public void setOperatingLng(String operatingLng) {
        this.operatingLng = operatingLng;
    }
    public String getOperatingLat() {
        return operatingLat;
    }
    public void setOperatingLat(String operatingLat) {
        this.operatingLat = operatingLat;
    }
    public String getOtherTitle() {
        return otherTitle;
    }
    public void setOtherTitle(String otherTitle) {
        this.otherTitle = otherTitle;
    }
    public String getMainCategory() {
        return mainCategory;
    }
    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }
    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public Long getCreatorId() {
        return creatorId;
    }
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public Long getModifierId() {
        return modifierId;
    }
    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }
    public LocalDateTime getModifyTime() {
        return modifyTime;
    }
    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }


    /**
     * 获取客户级别显示
     * @return
     */
    public String getGradeValue(){
        CustomerEnum.Grade instance = CustomerEnum.Grade.getInstance(this.getGrade());
        if (Objects.nonNull(instance)){
            return instance.getValue();
        }
        return "";
    }
}

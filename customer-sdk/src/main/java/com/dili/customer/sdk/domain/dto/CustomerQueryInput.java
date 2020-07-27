package com.dili.customer.sdk.domain.dto;

import com.dili.customer.sdk.domain.Customer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/5 14:14
 */
public class CustomerQueryInput extends Customer {

    /**
     * 证件号后模糊匹配查询
     */
    private String certificateNumberMatch;

    /**
     * 创建时间区间查询-开始
     */
    private LocalDate createTimeStart;
    /**
     * 创建时间区间查询-结束
     */
    private LocalDate createTimeEnd;

    /**
     * 客户所在市场中的创建时间-开始
     */
    private LocalDate marketCreateTimeStart;

    /**
     * 客户所在市场中的创建时间-介绍
     */
    private LocalDate marketCreateTimeEnd;

    /**
     * 客户等级
     * {@link com.dili.customer.sdk.enums.CustomerEnum.Grade}
     */
    private Integer grade;

    /**
     * 客户身份类型
     */
    private String customerType;

    /**
     * 归属部门
     */
    private Long departmentId;

    /**
     * 客户所属组织
     */
    private Long marketId;

    /**
     * 客户所属组织集
     */
    /*private List<Long> marketIdList = new ArrayList<>();*/

    /**
     * 根据ID集查询
     */
    private Set<Long> idSet = new HashSet<>();

    /**
     * 关键字查询，根据证件号匹配或名称模糊或编号前模糊查询
     */
    private String keyword;

    /**
     * 客户所在市场的客户创建人
     */
    private Long marketCreatorId;

    /**
     * 当客户在多市场时，是否分组只返回一条客户主数据
     * 如果设置为true，则根据客户id分组
     */
    private Boolean isGroup;

    /**
     * 根据ID不存在的条件集过滤
     */
    private Set<Long> notInIdSet = new HashSet<>();

    /**
     * 理货区货位号
     */
    private Set<Long> assetsIdSet = new HashSet<>();

    /**
     * 是否有(上传)营业执照
     */
    private Integer hasLicense;

    public String getCertificateNumberMatch() {
        return certificateNumberMatch;
    }
    public void setCertificateNumberMatch(String certificateNumberMatch) {
        this.certificateNumberMatch = certificateNumberMatch;
    }
    public LocalDate getCreateTimeStart() {
        return createTimeStart;
    }
    public void setCreateTimeStart(LocalDate createTimeStart) {
        this.createTimeStart = createTimeStart;
    }
    public LocalDate getCreateTimeEnd() {
        return createTimeEnd;
    }
    public void setCreateTimeEnd(LocalDate createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }
    public LocalDate getMarketCreateTimeStart() {
        return marketCreateTimeStart;
    }
    public void setMarketCreateTimeStart(LocalDate marketCreateTimeStart) {
        this.marketCreateTimeStart = marketCreateTimeStart;
    }
    public LocalDate getMarketCreateTimeEnd() {
        return marketCreateTimeEnd;
    }
    public void setMarketCreateTimeEnd(LocalDate marketCreateTimeEnd) {
        this.marketCreateTimeEnd = marketCreateTimeEnd;
    }
    public Integer getGrade() {
        return grade;
    }
    public void setGrade(Integer grade) {
        this.grade = grade;
    }
    public String getCustomerType() {
        return customerType;
    }
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
    public Long getMarketId() {
        return marketId;
    }
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }
/*    public List<Long> getMarketIdList() {
        return marketIdList;
    }
    public void setMarketIdList(List<Long> marketIdList) {
        this.marketIdList = marketIdList;
    }*/
    public Set<Long> getIdSet() {
        return idSet;
    }
    public void setIdSet(Set<Long> idSet) {
        this.idSet = idSet;
    }
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public Long getMarketCreatorId() {
        return marketCreatorId;
    }
    public void setMarketCreatorId(Long marketCreatorId) {
        this.marketCreatorId = marketCreatorId;
    }
    public Boolean getGroup() {
        return isGroup;
    }
    public void setGroup(Boolean group) {
        isGroup = group;
    }
    public Set<Long> getNotInIdSet() {
        return notInIdSet;
    }
    public void setNotInIdSet(Set<Long> notInIdSet) {
        this.notInIdSet = notInIdSet;
    }
    public Long getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    public Set<Long> getAssetsIdSet() {
        return assetsIdSet;
    }
    public void setAssetsIdSet(Set<Long> assetsIdSet) {
        this.assetsIdSet = assetsIdSet;
    }
    public Integer getHasLicense() {
        return hasLicense;
    }
    public void setHasLicense(Integer hasLicense) {
        this.hasLicense = hasLicense;
    }
}

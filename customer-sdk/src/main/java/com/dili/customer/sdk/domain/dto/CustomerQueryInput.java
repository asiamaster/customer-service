package com.dili.customer.sdk.domain.dto;

import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.validator.AddView;
import com.dili.customer.sdk.validator.UpdateView;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

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
     * 客户所属组织
     */
    private Long marketId;

    /**
     * 客户所属组织集
     */
    private List<Long> marketIdList;

    /**
     * 根据ID集查询
     */
    private List<Long> idList;

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
    public Long getMarketId() {
        return marketId;
    }
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }
    public List<Long> getMarketIdList() {
        return marketIdList;
    }
    public void setMarketIdList(List<Long> marketIdList) {
        this.marketIdList = marketIdList;
    }
    public List<Long> getIdList() {
        return idList;
    }
    public void setIdList(List<Long> idList) {
        this.idList = idList;
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
}

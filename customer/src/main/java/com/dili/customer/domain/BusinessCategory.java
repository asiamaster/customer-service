package com.dili.customer.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 客户经营品类信息
 * This file was generated on 2020-08-31 14:20:44.
 */
@Data
@Table(name = "`business_category`")
public class BusinessCategory extends BaseDomain {
    /**
     * ID
     */
    @Column(name = "`id`")
    private Long id;

    /**
     * 客户ID
     */
    @Column(name = "`customer_id`")
    private Long customerId;

    /**
     * 所属市场ID
     */
    @Column(name = "`market_id`")
    private Long marketId;

    /**
     * 经营品类ID路径
     */
    @Column(name = "`category_id`")
    private String categoryId;

    /**
     * 经营品类名称全路径
     */
    @Column(name = "`category_name`")
    private String categoryName;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`",updatable = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`modify_time`")
    private LocalDateTime modifyTime;

}
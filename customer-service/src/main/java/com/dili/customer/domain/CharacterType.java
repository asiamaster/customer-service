package com.dili.customer.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.domain.annotation.Operator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 客户角色身份信息
 * This file was generated on 2020-11-25 16:06:44.
 */
@Data
@Table(name = "`character_type`")
public class CharacterType extends BaseDomain {
    /**
     * ID
     */
    @Column(name = "`id`")
    private Long id;

    /**
     * 客户ID
     */
    @Column(name = "`customer_id`",updatable = false)
    private Long customerId;

    /**
     * 所属市场ID
     */
    @Column(name = "`market_id`",updatable = false)
    private Long marketId;

    /**
     * 客户角色身份
     */
    @Column(name = "`character_type`")
    private String characterType;

    /**
     * 客户子身份
     */
    @Column(name = "`sub_type`")
    private String subType;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`",updatable = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HHm:m:ss")
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    @Column(name = "`modify_time`")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HHm:m:ss")
    private LocalDateTime modifyTime;

}
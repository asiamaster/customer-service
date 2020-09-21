package com.dili.customer.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 客户联系地址信息
 * 
 * This file was generated on 2020-01-09 17:46:13.
 */
@Table(name = "`address`")
@Getter
@Setter
public class Address extends BaseDomain {
    /**
     * ID
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属客户
     */
    @Column(name = "`customer_id`", updatable = false)
    @NotNull(message = "所属客户不能为空")
    private Long customerId;

    /**
     * 客户地址所属市场
     */
    @Column(name = "`market_id`", updatable = false)
    @NotNull(message = "所属市场不能为空")
    private Long marketId;

    /**
     * 所在城市ID路径
     */
    @Column(name = "`city_path`")
    @NotNull(message = "所属城市不能为空")
    private String cityPath;

    /**
     * 所在城市名称(城市信息合并名称)
     */
    @Column(name = "`city_name`")
    @NotNull(message = "所属城市不能为空")
    private String cityName;

    /**
     * 地址
     */
    @Column(name = "`address`")
    @NotBlank(message = "联系地址不能为空")
    @Size(max = 250,message = "联系地址请保持在250个字符以内")
    private String address;

    /**
     * 是否现住址
     */
    @Column(name = "`is_current`")
    private Integer isCurrent;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`", updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    /**
     * 创建人
     */
    @Column(name = "`creator_id`",updatable = false)
    private Long creatorId;

    /**
     * 修改人
     */
    @Column(name = "`modifier_id`")
    @NotNull(message = "操作人不能为空")
    private Long modifierId;

}
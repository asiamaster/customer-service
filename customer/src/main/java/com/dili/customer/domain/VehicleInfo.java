package com.dili.customer.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.domain.annotation.Operator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 客户车辆信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/11/28 21:34
 */
@Data
@Table(name = "`vehicle_info`")
public class VehicleInfo extends BaseDomain {

    /**
     * ID
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户ID
     */
    @Column(name = "`customer_id`",updatable = false)
    @NotNull(message = "所属客户不能为空")
    private Long customerId;

    /**
     * 所属市场ID
     */
    @Column(name = "`market_id`",updatable = false)
    @NotNull(message = "所属市场不能为空")
    private Long marketId;

    /**
     * 注册车牌号
     */
    @Column(name = "`registration_number`")
    @NotBlank(message = "注册车牌号不能为空")
    private String registrationNumber;

    /**
     * 车型编号
     */
    @Column(name = "`type_number`")
    @NotNull(message = "注册车型不能为空")
    private Long typeNumber;

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

    /**
     * 客户数据群
     */
    @Transient
    @Column(name = "customer_id")
    @Operator(Operator.IN)
    private Set<Long> customerIdSet;
}

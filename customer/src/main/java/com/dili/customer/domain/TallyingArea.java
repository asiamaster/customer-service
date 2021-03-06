package com.dili.customer.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.domain.annotation.Operator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 客户理货区关联关系
 * This file was generated on 2020-07-15 15:08:13.
 */
@Table(name = "tallying_area")
@Getter
@Setter
@ToString(callSuper = true)
public class TallyingArea extends BaseDomain {
    /**
     * 主键ID
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户ID
     */
    @Column(name = "customer_id")
    private Long customerId;

    /**
     * 客户理货区市场
     */
    @Column(name = "market_id")
    private Long marketId;

    /**
     * 客户理货区所属部门
     */
    @Column(name = "department_id")
    private Long departmentId;

    /**
     * 客户理货区(资产)ID
     */
    @Column(name = "assets_id")
    private Long assetsId;

    /**
     * 理货区(资产)名称
     */
    @Column(name = "assets_name")
    private String assetsName;

    /**
     * 是否存在租赁关系
     */
    @Column(name = "is_lease")
    private Integer isLease;

    /**
     * 是否合法可用
     */
    @Column(name = "is_usable")
    private Integer isUsable;

    /**
     * 租赁开始时间
     */
    @Column(name = "start_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HHm:m:ss")
    private LocalDateTime startTime;

    /**
     * 租赁结束时间
     */
    @Column(name = "end_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HHm:m:ss")
    private LocalDateTime endTime;

    /**
     * 租赁状态（1：已创建 2：已取消 3：已提交 4：未生效 5：已生效 6：已停租 7：已退款 8：已过期）
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 创建时间
     */
    @Column(name = "create_time",updatable = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HHm:m:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HHm:m:ss")
    private LocalDateTime modifyTime;

    /**
     * 客户数据群
     */
    @Transient
    @Column(name = "customer_id")
    @Operator(Operator.IN)
    private Set<Long> customerIdSet;

}
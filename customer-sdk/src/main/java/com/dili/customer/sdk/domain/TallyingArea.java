package com.dili.customer.sdk.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 客户理货区
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/7/15 15:21
 */
@Data
public class TallyingArea extends BaseDomain {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户理货区市场
     */
    private Long marketId;

    /**
     * 客户理货区所属部门
     */
    private Long departmentId;

    /**
     * 客户理货区(资产)ID
     */
    private Long assetsId;

    /**
     * 理货区(资产)名称
     */
    private String assetsName;

    /**
     * 是否存在租赁关系
     */
    private Integer isLease;

    /**
     * 是否合法可用(有租赁状态且已生效，或者没有租赁状态 均认为是可用)
     */
    private Integer isUsable;

    /**
     * 租赁开始时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HHm:m:ss")
    private LocalDateTime startTime;

    /**
     * 租赁结束时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HHm:m:ss")
    private LocalDateTime endTime;

    /**
     * 租赁状态（1：已创建 2：已取消 3：已提交 4：未生效 5：已生效 6：已停租 7：已退款 8：已过期）
     */
    private Integer state;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HHm:m:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HHm:m:ss")
    private LocalDateTime modifyTime;

    /**
     * 客户数据集
     */
    private Set<Long> customerIdSet;

}

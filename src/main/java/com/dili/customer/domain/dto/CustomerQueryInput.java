package com.dili.customer.domain.dto;

import com.dili.customer.domain.Customer;
import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <B>客户信息查询输入项</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/4 16:21
 */
@Getter
@Setter
@ToString(callSuper = true)
public class CustomerQueryInput extends Customer {

    /**
     * 创建时间区间查询-开始
     */
    @Column(name = "create_time")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private LocalDateTime createTimeStart;
    /**
     * 创建时间区间查询-结束
     */
    @Column(name = "create_time")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private LocalDateTime createTimeEnd;

    @Column(name = "certificate_number")
    @Like(value = Like.RIGHT)
    private String certificateNumberMatch;

    /**
     * 客户所在市场中的创建时间-开始
     */
    private LocalDateTime marketCreateTimeStart;

    /**
     * 客户所在市场中的创建时间-介绍
     */
    private LocalDateTime marketCreateTimeEnd;

    /**
     * 客户所属组织
     */
    private Long marketId;

    /**
     * 客户所属组织集
     */
    private List<Long> marketIdList;

    /**
     * 关键字查询，根据证件号匹配或名称模糊查询
     */
    private String keyword;

    /**
     * 所在市场的创建人
     */
    private Long marketCreatorId;

    /**
     * 当客户在多市场时，是否分组只返回一条客户主数据
     * 如果设置为true，则根据客户id分组
     */
    private Boolean isGroup;
}

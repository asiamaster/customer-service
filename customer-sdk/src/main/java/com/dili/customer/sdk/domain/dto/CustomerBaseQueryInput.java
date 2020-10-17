package com.dili.customer.sdk.domain.dto;

import com.dili.customer.sdk.domain.Customer;
import lombok.Data;

import java.time.LocalDate;
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
@Data
public class CustomerBaseQueryInput extends CustomerExtendDto {

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
     * 根据ID集查询
     */
    private Set<Long> idSet = new HashSet<>();

    /**
     * 关键字查询，根据证件号匹配或名称模糊或编号前模糊查询
     */
    private String keyword;

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
     * 当前城市ID
     */
    private Integer currentCityId;

    /**
     * 客户编号列表
     */
    private List<String> codeList;
}

package com.dili.customer.sdk.domain.query;

import com.dili.customer.sdk.domain.CharacterType;
import com.dili.customer.sdk.domain.Customer;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/2/3 16:34
 */
@Data
public class CustomerBaseQueryInput extends Customer {

    /**
     * 证件号后模糊匹配查询
     */
    private String certificateNumberMatch;

    /**
     * 根据ID集查询
     */
    private Set<Long> idSet = new HashSet<>();

    /**
     * 关键字查询，根据证件号匹配或名称模糊或编号前模糊查询
     */
    private String keyword;

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

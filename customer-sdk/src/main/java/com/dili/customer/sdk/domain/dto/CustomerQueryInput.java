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
public class CustomerQueryInput extends CustomerBaseQueryInput {

    /**
     * 客户所在市场中的创建时间-开始
     */
    private LocalDate marketCreateTimeStart;

    /**
     * 客户所在市场中的创建时间-介绍
     */
    private LocalDate marketCreateTimeEnd;

    /**
     * 客户所属组织
     */
    private Long marketId;

    /**
     * 理货区货位号
     */
    private Set<Long> assetsIdSet = new HashSet<>();

    /**
     * 是否有(上传)营业执照
     */
    private Integer hasLicense;

}

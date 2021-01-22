package com.dili.customer.sdk.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
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
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime marketCreateTimeStart;

    /**
     * 客户所在市场中的创建时间-介绍
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime marketCreateTimeEnd;

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

    /**
     * 客户车牌号
     */
    private String vehicleNumber;

    /**
     * 资产名称(摊位、公寓等名称)
     */
    private String assetsName;

}

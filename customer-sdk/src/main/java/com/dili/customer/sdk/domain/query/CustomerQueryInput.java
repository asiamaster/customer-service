package com.dili.customer.sdk.domain.query;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.customer.sdk.domain.CharacterType;
import com.dili.customer.sdk.domain.CustomerMarket;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/2/3 16:38
 */
@Data
public class CustomerQueryInput extends CustomerBaseQueryInput{
    /**
     * 客户所在市场中的创建时间-开始
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime marketCreateTimeStart;

    /**
     * 客户所在市场中的创建时间-结束
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
     * 客户身份类型
     */
    private CharacterType characterType;

    /**
     * 客户所在市场信息
     */
    private CustomerMarket customerMarket;

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

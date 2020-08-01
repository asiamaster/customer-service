package com.dili.customer.sdk.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 客户地址信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/7/15 18:50
 */
@Data
public class Address extends BaseDomain {

    /**
     * ID
     */
    private Long id;

    /**
     * 所属客户
     */
    @NotNull(message = "所属客户不能为空")
    private Long customerId;

    /**
     * 客户地址所属市场
     */
    @NotNull(message = "所属市场不能为空")
    private Long marketId;

    /**
     * 所在城市
     */
    @NotNull(message = "所属城市不能为空")
    private String cityId;

    /**
     * 所在城市名称(城市信息合并名称)
     */
    @NotNull(message = "所属城市不能为空")
    private String cityName;

    /**
     * 地址
     */
    @NotBlank(message = "联系地址不能为空")
    @Size(max = 250,message = "联系地址请保持在250个字符以内")
    private String address;

    /**
     * 是否现住址
     * {@link com.dili.commons.glossary.YesOrNoEnum}
     */
    private Integer isCurrent;

    /**
     * 是否默认
     * {@link com.dili.commons.glossary.YesOrNoEnum}
     */
    private Integer isDefault;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    /**
     * 创建人
     */
    private Long creatorId;

    /**
     * 修改人
     */
    private Long modifierId;

}

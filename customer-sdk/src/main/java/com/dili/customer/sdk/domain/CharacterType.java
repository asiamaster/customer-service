package com.dili.customer.sdk.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 客户角色身份信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/11/25 16:58
 */
@Data
public class CharacterType extends BaseDomain {

    /**
     * ID
     */
    private Long id;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 所属市场ID
     */
    private Long marketId;

    /**
     * 客户角色身份
     * {@link com.dili.customer.sdk.enums.CustomerEnum.CharacterType}
     */
    @NotBlank(message = "客户身份信息不能为空")
    private String characterType;

    /**
     * 客户子身份
     */
    private String subType;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HHm:m:ss")
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HHm:m:ss")
    private LocalDateTime modifyTime;
}

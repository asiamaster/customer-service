package com.dili.customer.sdk.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/7/25 17:31
 */
@Data
public class Attachment extends BaseDomain {

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
     * 附件类型
     * {@link com.dili.customer.sdk.enums.CustomerEnum.AttachmentType}
     */
    private Integer fileType;

    /**
     * 附件地址
     */
    private String address;

    /**
     * 文件名称
     */
    private String fileName;

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
     * 客户ID集
     */
    private Set<Long> customerIdSet;

}

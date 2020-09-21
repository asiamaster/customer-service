package com.dili.customer.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 由MyBatis Generator工具自动生成
 * 客户上传的附件信息
 * This file was generated on 2020-07-25 17:14:18.
 */
@Table(name = "attachment")
@Getter
@Setter
@ToString(callSuper = true)
public class Attachment extends BaseDomain {
    /**
     * ID
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户ID
     */
    @Column(name = "customer_id")
    private Long customerId;

    /**
     * 所属市场ID
     */
    @Column(name = "market_id")
    private Long marketId;

    /**
     * 附件类型
     * {@link com.dili.customer.sdk.enums.CustomerEnum.AttachmentType}
     */
    @Column(name = "file_type")
    private Integer fileType;

    /**
     * 附件地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 文件名称
     */
    @Column(name = "file_name")
    private String fileName;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HHm:m:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HHm:m:ss")
    private LocalDateTime modifyTime;
}
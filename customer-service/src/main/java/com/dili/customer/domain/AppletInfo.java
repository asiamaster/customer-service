package com.dili.customer.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 小程序信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/29 9:53
 */
@Entity
@Table(name = "applet_info")
@Getter
@Setter
public class AppletInfo extends BaseDomain {

    /**
     * ID
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 小程序名称
     */
    @Column(name = "`applet_name`")
    private String appletName;
    /**
     * 所属系统
     */
    @Column(name = "`system_code`")
    private String systemCode;
    /**
     * 内部编码
     */
    @Column(name = "`applet_code`")
    private String appletCode;
    /**
     * 小程序appId
     */
    @Column(name = "`app_id`")
    private String appId;
    /**
     * 小程序密码
     */
    @Column(name = "`secret`")
    private String secret;
    /**
     * 小程序类型
     * {@link com.dili.customer.enums.AppletTerminalType}
     */
    @Column(name = "`applet_type`")
    private Integer appletType;
    /**
     * 创建时间
     */
    @Column(name = "`create_time`", updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;
}

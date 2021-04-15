package com.dili.customer.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.dao.sql.DateNextVersion;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户账号绑定的终端
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/28 17:53
 */
@Accessors(chain = true)
@Data
@Table(name = "account_terminal")
public class AccountTerminal extends BaseDomain implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    @Accessors(chain = false)
    private Long id;

    /**
     * 账号
     */
    @Column(name = "account_id")
    @NotNull(message = "所属客户账号")
    private Long accountId;


    /**
     * 第三方appId
     */
    @Column(name = "app_id")
    private String appId;

    /**
     * 终端号类型
     */
    @Column(name = "terminal_type")
    private Integer terminalType;

    /**
     * 终端号(openId)
     */
    @Column(name = "terminal_code")
    private String terminalCode;

    /**
     * 终端昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 头像地址
     */
    @Column(name = "avatar_url")
    private String avatarUrl;

    /**
     * 创建时间
     */
    @Column(name = "create_time", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Version(nextVersion = DateNextVersion.class)
    private LocalDateTime modifyTime;

    /**
     * copy 源对象到当前对象方法
     * @param source
     */
    public void copy(AccountTerminal source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}

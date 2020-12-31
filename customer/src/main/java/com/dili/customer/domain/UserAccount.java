package com.dili.customer.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.annotation.JSONField;
import com.dili.customer.sdk.validator.AddView;
import com.dili.ss.dao.sql.DateNextVersion;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/8 11:25
 */
@Accessors(chain = true)
@Entity
@Data
@Table(name = "user_account")
public class UserAccount extends BaseDomain implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    @Accessors(chain = false)
    private Long id;

    /**
     * 客户ID
     */
    @Column(name = "customer_id", updatable = false)
    @NotNull(message = "客户不能为空", groups = AddView.class)
    private Long customerId;

    /**
     * 客户证件号
     */
    @Column(name = "certificate_number")
    @NotBlank(message = "客户证件不能为空", groups = AddView.class)
    private String certificateNumber;

    /**
     * 客户编码
     */
    @Column(name = "customer_code", updatable = false)
    @NotBlank(message = "客户编码不能为空", groups = AddView.class)
    private String customerCode;

    /**
     * 手机号
     */
    @Column(name = "cellphone")
    @NotBlank(message = "手机号不能为空", groups = AddView.class)
    private String cellphone;

    /**
     * 手机号是否已短信认证
     * {@link com.dili.commons.glossary.YesOrNoEnum}
     */
    @Column(name = "cellphone_valid")
    private Integer cellphoneValid;

    /**
     * 账户名称
     */
    @Column(name = "account_name")
    @NotBlank(message = "名称不能为空", groups = AddView.class)
    private String accountName;

    /**
     * 账号
     */
    @Column(name = "account_code", updatable = false)
    @NotNull(message = "客户账号", groups = AddView.class)
    private String accountCode;

    /**
     * 密码
     */
    @Column(name = "password")
    @JsonIgnore
    private String password;

    /**
     * 启用状态
     */
    @Column(name = "is_enable")
    private Integer isEnable;

    /**
     * 备注
     */
    @Column(name = "notes")
    @Size(max = 100, message = "备注信息请保持在100个字以内")
    private String notes;

    /**
     * 密码修改时间
     */
    @Column(name = "changed_pwd_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime changedPwdTime;

    /**
     * 是否已删除
     * {@link com.dili.commons.glossary.YesOrNoEnum}
     */
    @Column(name = "deleted")
    private Integer deleted;

    /**
     * 账号合并后的ID
     */
    @Column(name = "new_account_id")
    @JsonIgnore
    private Long newAccountId;

    /**
     * 创建人ID
     */
    @Column(name = "operator_id", updatable = false)
    private Long operatorId;

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
    public void copy(UserAccount source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}

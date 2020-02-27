package com.dili.customer.domain.dto;

import com.dili.customer.validator.AddView;
import com.dili.customer.validator.UpdateView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/21 18:01
 */
@Getter
@Setter
@ToString
public class IndividualCustomerInput implements Serializable {
    private static final long serialVersionUID = 3300214922552754245L;

    /**
     * 客户ID
     */
    @NotNull(message = "业务关键ID不能为空",groups = {UpdateView.class})
    private Long id;

    /**
     * 组织类型,个人/企业
     */
    @NotBlank(message = "组织类型不能为空", groups = {AddView.class})
    private String organizationType;

    /**
     * 证件类型
     */
    @NotBlank(message = "证件类型不能为空", groups = {AddView.class})
    private String certificateType;

    /**
     * 证件号
     */
    @NotBlank(message = "证件号码不能为空", groups = {AddView.class})
    @Size(min = 1, max = 20, message = "证件号码请保持在40个字以内", groups = {AddView.class})
    private String certificateNumber;

    /**
     * 客户名称
     */
    @NotBlank(message = "客户名称不能为空", groups = {AddView.class,UpdateView.class})
    @Size(min = 1, max = 40, message = "客户名称请保持在40个字以内", groups = {AddView.class,UpdateView.class})
    private String name;

    /**
     * 客户等级
     */
    @NotNull(message = "客户等级不能为空", groups = {AddView.class,UpdateView.class})
    private Integer grade;

    /**
     * 来源系统##外部系统来源标识
     */
    @NotBlank(message = "客户来源系统不能为空", groups = {AddView.class})
    private String sourceSystem;

    /**
     * 来源渠道##摊位租赁、系统注册等
     */
    @NotBlank(message = "客户来源渠道不能为空", groups = {AddView.class})
    private String sourceChannel;


    /**
     * 创建人
     */
    @NotNull(message = "操作人ID不能为空", groups = {AddView.class, UpdateView.class})
    private Long operatorId;

    /**
     * 客户归属人
     */
    @NotNull(message = "客户归属人不能为空", groups = {AddView.class, UpdateView.class})
    private Long ownerId;

    /**
     * 客户所属市场
     */
    @NotNull(message = "所属市场不能为空", groups = {AddView.class,UpdateView.class})
    private Long marketId;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空", groups = {AddView.class,UpdateView.class})
    @Pattern(regexp = "^(1[3456789]\\d{9})$", message = "请输入正确的联系方式", groups = {AddView.class,UpdateView.class})
    private String contactsPhone;

    /**
     * 客户状态
     */
    @NotNull(message = "客户状态不能为空", groups = {UpdateView.class})
    private Integer state;

    /**
     * 归属部门
     */
    private Long departmentId;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 照片
     */
    private String photo;

    /**
     * 出生日期
     */
    private LocalDate birthdate;

    /**
     * 证件地址
     */
    private String certificateAddr;

    /**
     * 证件日期##企业时为营业执照日期,如:2011-09-01 至 长期
     */
    private String certificateRange;

}

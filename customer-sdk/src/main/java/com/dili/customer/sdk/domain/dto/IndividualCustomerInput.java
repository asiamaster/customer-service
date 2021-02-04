package com.dili.customer.sdk.domain.dto;


import com.dili.customer.sdk.constants.ValidatedConstant;
import com.dili.customer.sdk.domain.BusinessCategory;
import com.dili.customer.sdk.domain.CharacterType;
import com.dili.customer.sdk.domain.CustomerMarket;
import com.dili.customer.sdk.domain.TallyingArea;
import com.dili.customer.sdk.validator.AddView;
import com.dili.customer.sdk.validator.CompleteView;
import com.dili.customer.sdk.validator.UpdateView;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * <B>客户新增保存时的基本信息</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/4 9:53
 */
@Data
public class IndividualCustomerInput implements Serializable {
    private static final long serialVersionUID = -5865840494367827998L;

    /**
     * 客户ID
     */
    @NotNull(message = "业务关键ID不能为空", groups = {UpdateView.class, CompleteView.class})
    private Long id;

    /**
     * 证件号
     */
    @NotBlank(message = "(企业/个人)证件号码不能为空", groups = {AddView.class, CompleteView.class})
    @Size(min = 1, max = 40, message = "(企业/个人)证件号码请保持在40个字以内", groups = {AddView.class, CompleteView.class})
    @Pattern(regexp = ValidatedConstant.CUSTOMER_CERTIFICATE_NUMBER_VALID_REGEXP, message = "请输入正确的(企业/个人)证件号码", groups = {AddView.class,CompleteView.class})
    private String certificateNumber;

    /**
     * 证件类型
     */
    @NotBlank(message = "证件类型不能为空", groups = {AddView.class, CompleteView.class})
    private String certificateType;

    /**
     * 客户名称
     */
    @NotBlank(message = "客户名称不能为空", groups = {AddView.class,UpdateView.class})
    @Size(min = 1, max = 40, message = "客户名称请保持在40个字以内", groups = {AddView.class,UpdateView.class})
    @Pattern(regexp = ValidatedConstant.CUSTOMER_NAME_VALID_REGEXP, message = "请输入正确的客户姓名", groups = {AddView.class, UpdateView.class})
    private String name;

    /**
     * 组织类型,个人/企业
     */
    @NotBlank(message = "组织类型不能为空", groups = {AddView.class})
    private String organizationType;

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
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空", groups = {AddView.class,UpdateView.class})
    @Pattern(regexp = ValidatedConstant.CUSTOMER_CELLPHONE_VALID_REGEXP, message = "请输入正确的联系方式", groups = {AddView.class, UpdateView.class})
    private String contactsPhone;

    /**
     * 客户编码
     */
    private String code;

    /**
     * 创建人
     */
    private Long operatorId;

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

    /**
     * 证件是否长期有效 1-是；0-否
     */
    private Integer certificateLongTerm;

    /**
     * 现住址城市ID路径
     * 格式为 省(编码),市(编码),区(编码) 如: 100000,110000,111100
     */
    private String currentCityPath;

    /**
     * 现住址城市名称
     * 格式为 省(中文),市(中文),区(中文) 如：四川省,成都市,青羊区
     */
    private String currentCityName;

    /**
     * 现住址详细地址
     */
    private String currentAddress;

    /**
     * 客户所属市场信息
     */
    @Valid
    private CustomerMarket customerMarket;

    /**
     * 客户理货区
     */
    private List<TallyingArea> tallyingAreaList;

    /**
     * 紧急联系人
     */
    private String emergencyContactsName;

    /**
     * 紧急联系电话
     */
    private String emergencyContactsPhone;

    /**
     * 客户经营品类信息
     */
    @Valid
    private List<BusinessCategory> businessCategoryList;

    /**
     * 手机号是否已认证  1-是 0-否
     * {@link com.dili.commons.glossary.YesOrNoEnum}
     */
    @NotNull(message = "【手机号是否已认证】不能为空")
    private Integer isCellphoneValid;

    /**
     * 客户身份类型
     */
    @NotNull(message = "客户身份类型不能为空")
    private List<CharacterType> characterTypeList;

    /**
     * 是否已实名认证  1-是 0-否
     * {@link com.dili.commons.glossary.YesOrNoEnum}
     */
    @NotNull(message = "【是否已实名】不能为空")
    private Integer isCertification;

}

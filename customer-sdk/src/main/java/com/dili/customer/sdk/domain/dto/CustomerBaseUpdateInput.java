package com.dili.customer.sdk.domain.dto;

import com.dili.customer.sdk.domain.BusinessCategory;
import com.dili.customer.sdk.domain.CharacterType;
import com.dili.customer.sdk.domain.CustomerMarket;
import com.dili.customer.sdk.domain.TallyingArea;
import com.dili.customer.sdk.validator.CompleteView;
import com.dili.customer.sdk.validator.UpdateView;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/19 16:05
 */
@Data
public class CustomerBaseUpdateInput {

    /**
     * 客户ID
     */
    @NotNull(message = "业务关键ID不能为空")
    private Long id;

    /**
     * 客户姓名
     */
    @NotBlank(message = "客户姓名不能为空")
    private String name;

    /**
     * 证件类型
     */
    @NotBlank(message = "客户证件类型不能为空", groups = {CompleteView.class})
    private String certificateType;

    /**
     * 证件号
     */
    @NotBlank(message = "(企业/个人)客户证件号不能为空", groups = {CompleteView.class})
    @Size(min = 1, max = 40, message = "(企业/个人)证件号码请保持在40个字以内")
    @Pattern(regexp = "^[()a-z0-9A-Z\\u4e00-\\u9fa5]+$", message = "请输入正确的(企业/个人)客户证件号码", groups = {CompleteView.class})
    private String certificateNumber;

    /**
     * 客户状态 0注销，1生效，2禁用，
     */
    @NotNull(message = "客户状态不能为空",groups = {UpdateView.class})
    private Integer state;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空",groups = {UpdateView.class})
    @Pattern(regexp = "^(1[3456789]\\d{9})$", message = "请输入正确的联系方式")
    private String contactsPhone;

    /**
     * 客户组织类型(个人、企业等)
     */
    @NotBlank(message = "组织类型不能为空")
    private String organizationType;

    /**
     * 客户市场信息
     */
    @Valid
    @NotNull(message = "市场信息不能为空")
    private CustomerMarket customerMarket;

    /**
     * 操作人ID
     */
    @NotNull(message = "操作人不能为空", groups = {UpdateView.class})
    private Long operatorId;

    /**
     * 客户身份信息
     */
    @NotNull(message = "客户角色身份信息不能为空")
    @Size(min = 1, message = "客户角色身份信息不能为空")
    private List<CharacterType> characterTypeList;

    /**
     * 客户经营品类信息
     */
    @Valid
    private List<BusinessCategory> businessCategoryList;

    /**
     * 客户理货区
     */
    @Valid
    private List<TallyingArea> tallyingAreaList;
}

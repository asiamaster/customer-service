package com.dili.customer.sdk.domain.dto;

import com.dili.customer.sdk.domain.*;
import com.dili.customer.sdk.validator.CompleteView;
import com.dili.customer.sdk.validator.UpdateView;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/27 22:21
 */
@Data
public class CustomerUpdateInput implements Serializable {

    private static final long serialVersionUID = 5391152354038473151L;

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
    @NotBlank(message = "客户证件号不能为空", groups = {CompleteView.class})
    @Size(min = 1, max = 40, message = "证件号码请保持在40个字以内")
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
    private CustomerMarket customerMarket;

    /**
     * 证件信息
     */
    @Valid
    private CustomerCertificateInput customerCertificate;

    /**
     * 联系人信息
     */
    @Valid
    private List<Contacts> contactsList;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 客户理货区
     */
    private List<TallyingArea> tallyingAreaList;

    /**
     * 客户地址
     */
    private List<Address> addressList;

    /**
     * 客户经营品类信息
     */
    private List<BusinessCategory> businessCategoryList;

    /**
     * 客户身份信息
     */
    @NotNull
    private List<CharacterType> characterTypeList;

    /**
     * 客户车型信息
     */
    @Valid
    private List<VehicleInfo> vehicleInfoList;

    /**
     * 客户附件信息
     */
    @Valid
    private List<Attachment> attachmentList;

}

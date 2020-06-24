package com.dili.customer.domain.dto;

import com.dili.customer.domain.Contacts;
import com.dili.customer.domain.CustomerMarket;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/4 14:09
 */
@Getter
@Setter
@ToString(callSuper = true)
public class CustomerUpdateInput implements Serializable {

    private static final long serialVersionUID = -8304683405357244385L;

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
     * 客户状态 0注销，1生效，2禁用，
     */
    @NotNull(message = "客户状态不能为空")
    private Integer state;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^(1[3456789]\\d{9})$", message = "请输入正确的联系方式")
    private String contactsPhone;

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
    private List<Contacts> contactsList;

    /**
     * 操作人ID
     */
    private Long operatorId;

}

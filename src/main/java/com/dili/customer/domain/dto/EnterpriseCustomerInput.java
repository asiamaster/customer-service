package com.dili.customer.domain.dto;

import com.dili.customer.validator.AddView;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/21 18:40
 */
@Getter
@Setter
@ToString(callSuper = true)
public class EnterpriseCustomerInput extends IndividualCustomerInput implements Serializable {

    private static final long serialVersionUID = -2571148515605329127L;

    /**
     * 联系人
     */
    @NotBlank(message = "联系人不能为空", groups = {AddView.class})
    @Size(max = 40, message = "联系人请保持在250个字以内", groups = {AddView.class})
    private String contactsName;

    /**
     * 企业法人
     */
    @NotBlank(message = "企业法人不能为空", groups = {AddView.class})
    @Size(max = 40, message = "法人请保持在40个字以内", groups = {AddView.class})
    private String corporationName;
}

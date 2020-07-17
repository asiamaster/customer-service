package com.dili.customer.sdk.domain.dto;

import com.dili.customer.sdk.validator.AddView;
import com.dili.customer.sdk.validator.EnterpriseAddView;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * <B>企业客户新增保存时基本信息</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/3 17:40
 */
public class EnterpriseCustomerInput extends IndividualCustomerInput implements Serializable {

    private static final long serialVersionUID = 4869002082778403248L;

    /**
     * 联系人
     */
    @NotBlank(message = "联系人不能为空", groups = {EnterpriseAddView.class})
    @Size(max = 40, message = "联系人请保持在40个字以内", groups = {EnterpriseAddView.class})
    private String contactsName;

    /**
     * 企业法人
     */
    @NotBlank(message = "企业法人不能为空", groups = {EnterpriseAddView.class})
    @Size(max = 40, message = "法人请保持在40个字以内", groups = {EnterpriseAddView.class})
    private String corporationName;

    public String getContactsName() {
        return contactsName;
    }
    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }
    public String getCorporationName() {
        return corporationName;
    }
    public void setCorporationName(String corporationName) {
        this.corporationName = corporationName;
    }
}

package com.dili.customer.sdk.domain.dto;

import com.dili.customer.sdk.domain.Address;
import com.dili.customer.sdk.domain.Contacts;
import com.dili.customer.sdk.domain.CustomerMarket;
import com.dili.customer.sdk.domain.TallyingArea;

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
 * @date 2020/2/27 22:21
 */
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

    /**
     * 客户理货区
     */
    private List<TallyingArea> tallyingAreaList;

    /**
     * 客户地址
     */
    private List<Address> addressList;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }
    public String getContactsPhone() {
        return contactsPhone;
    }
    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }
    public CustomerMarket getCustomerMarket() {
        return customerMarket;
    }
    public void setCustomerMarket(CustomerMarket customerMarket) {
        this.customerMarket = customerMarket;
    }
    public CustomerCertificateInput getCustomerCertificate() {
        return customerCertificate;
    }
    public void setCustomerCertificate(CustomerCertificateInput customerCertificate) {
        this.customerCertificate = customerCertificate;
    }
    public List<Contacts> getContactsList() {
        return contactsList;
    }
    public void setContactsList(List<Contacts> contactsList) {
        this.contactsList = contactsList;
    }
    public Long getOperatorId() {
        return operatorId;
    }
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }
    public List<TallyingArea> getTallyingAreaList() {
        return tallyingAreaList;
    }
    public void setTallyingAreaList(List<TallyingArea> tallyingAreaList) {
        this.tallyingAreaList = tallyingAreaList;
    }
    public List<Address> getAddressList() {
        return addressList;
    }
    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}

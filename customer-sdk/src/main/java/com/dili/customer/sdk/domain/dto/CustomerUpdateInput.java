package com.dili.customer.sdk.domain.dto;

import com.dili.customer.sdk.domain.Address;
import com.dili.customer.sdk.domain.Attachment;
import com.dili.customer.sdk.domain.Contacts;
import com.dili.customer.sdk.domain.VehicleInfo;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/27 22:21
 */
@Data
public class CustomerUpdateInput extends CustomerBaseUpdateInput {

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
     * 客户地址
     */
    private List<Address> addressList;

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

    /**
     * 要删除的客户车型信息
     */
    private Set<Long> deletedVehicleInfoIds;
}

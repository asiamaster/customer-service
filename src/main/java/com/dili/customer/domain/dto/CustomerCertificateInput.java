package com.dili.customer.domain.dto;

import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/1/2 11:22
 */
@Getter
@Setter
public class CustomerCertificateInput implements Serializable {

    private static final long serialVersionUID = 2149328387032198883L;

    /**
     * 客户ID
     */
    @NotNull(message = "客户ID不能为空")
    private Long id;

    /**
     * 证件有效期
     */
    @Size(max = 40,message = "证件有效期请保持在40个字符以内")
    private String certificateRange;

    /**
     * 证件是否长期有效 1-是；0-否
     */
    private Integer certificateLongTerm;

    /**
     * 证件地址
     */
    @Size(max = 100,message = "证件有效期请保持在40个字符以内")
    private String certificateAddr;

    /**
     * 法人证件类型
     */
    @Size(max = 20,message = "法人证件类型请保持在40个字符以内")
    private String corporationCertificateType;

    /**
     * 法人证件号
     */
    @Size(max = 40,message = "法人证件号码请保持在40个字符以内")
    private String corporationCertificateNumber;

    /**
     * 法人真实姓名
     */
    @Size(max = 40,message = "法人姓名请保持在40个字符以内")
    private String corporationName;


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("certificateRange", certificateRange)
                .add("certificateAddr", certificateAddr)
                .add("corporationCertificateType", corporationCertificateType)
                .add("corporationCertificateNumber", corporationCertificateNumber)
                .add("corporationName", corporationName)
                .toString();
    }
}

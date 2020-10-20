package com.dili.customer.domain.dto;

import com.dili.customer.domain.CustomerMarket;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/10/16 11:36
 */
@Getter
@Setter
public class CustomerMarketDto extends CustomerMarket {

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户状态
     */
    private Integer customerState;

    /**
     * 手机号是否认证 1-是 0-否
     * {@link com.dili.commons.glossary.YesOrNoEnum}
     */
    private Integer isCellphoneValid;

    /**
     * 证件号
     */
    private String certificateNumber;

    /**
     * 组织类型,个人/企业
     */
    private String organizationType;
}

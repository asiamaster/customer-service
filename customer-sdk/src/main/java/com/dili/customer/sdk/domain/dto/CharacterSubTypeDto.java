package com.dili.customer.sdk.domain.dto;

import lombok.Data;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/18 21:45
 */
@Data
public class CharacterSubTypeDto {

    /**
     * 子角色编码
     */
    private String code;
    /**
     * 子角色名称
     */
    private String name;

    /**
     * 此自角色是否选中
     */
    private Boolean selected = Boolean.FALSE;
}

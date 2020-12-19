package com.dili.customer.sdk.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/18 21:37
 */
@Data
@Accessors(chain = true)
public class CharacterTypeGroupDto {

    /**
     * 角色编码
     */
    private String code;
    /**
     * 角色值
     */
    private String value;
    /**
     * 子角色是否多选
     */
    private Boolean multiple;
    /**
     * 此角色是否选中
     */
    private Boolean selected = Boolean.FALSE;

    /**
     * 子角色信息
     */
    private List<CharacterSubTypeDto> subTypeList = new ArrayList<>();
}

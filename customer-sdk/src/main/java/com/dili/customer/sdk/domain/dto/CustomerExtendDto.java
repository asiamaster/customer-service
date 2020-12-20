package com.dili.customer.sdk.domain.dto;

import com.dili.customer.sdk.domain.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/10/17 10:33
 */
@Getter
@Setter
public class CustomerExtendDto extends Customer {

    /**
     * 客户所在市场信息
     * 客户市场本身是一对多的关系，而此处使用单个对象，是按一个客户一个市场信息的单一关系返回
     */
    private CustomerMarket customerMarket;

    /**
     * 客户证件号打码加*显示
     */
    private String certificateNumberMask;

    /**
     * 客户理货区
     */
    private List<TallyingArea> tallyingAreaList;

    /**
     * 客户角色身份信息
     */
    private List<CharacterType> characterTypeList;

    /**
     * 客户市场身份
     * 分组转换一对多关系
     */
    private List<CharacterTypeGroupDto> characterTypeGroupList;

    /**
     * 客户车辆信息
     */
    private List<VehicleInfo> vehicleInfoList;

    /**
     * 客户附件分组显示信息
     */
    private List<AttachmentGroupInfo> attachmentGroupInfoList;
}

package com.dili.customer.sdk.domain.dto;

import com.dili.customer.sdk.domain.Attachment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 客户附件分组显示信息
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/17 17:27
 */
@Getter
@Setter
public class AttachmentGroupInfo {

    /**
     * 附件类型
     */
    private Integer code;

    /**
     * 附件类型名称
     */
    private String value;

    /**
     * 客户附件列表
     */
    private List<Attachment> attachmentList;
}

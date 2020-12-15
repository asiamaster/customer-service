package com.dili.customer.sdk.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.sdk.validator.CompleteView;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <B></B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/6/18 15:53
 */
@Data
public class CustomerMarket extends BaseDomain {

    /**
     * ID
     */
    private Long id;

    /**
     * 归属组织
     */
    @NotNull(message = "归属市场不能为空")
    private Long marketId;

    /**
     * 归属部门##内部创建归属到创建员工的部门
     */
    private Long departmentId;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 所有者
     */
    private Long ownerId;

    /**
     * 客户等级
     * {@link com.dili.customer.sdk.enums.CustomerEnum.Grade}
     */
    private Integer grade;

    /**
     * 客户行业代码,多个以英文逗号隔开
     */
    private String profession;

    /**
     * 客户行业名称,多个以英文逗号隔开
     */
    private String professionName;

    /**
     * 客户经营性质
     */
    @NotBlank(message = "客户经营性质不能为空", groups = {CompleteView.class})
    @Size(max = 120, message = "客户经营性质 请保持在120个字符以内")
    private String businessNature;

    /**
     * 经营地区##经营地区城市id
     */
    private String operatingArea;

    /**
     * 经营地区经度
     */
    private String operatingLng;

    /**
     * 经营地区纬度
     */
    private String operatingLat;

    /**
     * 其它头衔
     */
    private String otherTitle;

    /**
     * 销售市场
     */
    private String salesMarket;

    /**
     * 客户别名
     */
    private String alias;

    /**
     * 客户资料审核状态
     * {@link com.dili.customer.sdk.enums.CustomerEnum.ApprovalStatus}
     */
    private Integer approvalStatus;

    /**
     * 客户资料审核人
     */
    private Long approvalUserId;

    /**
     * 客户资料审核时间
     */
    private LocalDateTime approvalTime;

    /**
     * 备注信息
     */
    private String notes;

    /**
     * 在本市场的初始创建人
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改人id
     */
    private Long modifierId;

    /**
     * 修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;


    /**
     * 获取客户级别显示
     * @return
     */
    public String getGradeValue(){
        CustomerEnum.Grade instance = CustomerEnum.Grade.getInstance(this.getGrade());
        if (Objects.nonNull(instance)){
            return instance.getValue();
        }
        return "";
    }
}

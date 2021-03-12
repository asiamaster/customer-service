package com.dili.customer.sdk.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.sdk.validator.CompleteView;
import com.dili.customer.sdk.validator.UpdateView;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
     * 客户所属部门
     * 从1.3.5版本开始，由于现在调整为一对多的关系，所以此字段返回的数据废弃,也不会返回该字段值
     * {@link #departmentIds}
     * 后续版本中，此字段会被移除
     */
    @Deprecated
    private Long departmentId;

    /**
     * 归属部门##内部创建归属到创建员工的部门，多个以逗号隔开
     * 作为查询条件时，按单个整体处理
     */
    private String departmentIds;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 客户所属人
     * 从1.3.5版本开始，由于现在调整为一对多的关系，所以此字段返回的数据废弃,也不会返回该字段值
     * {@link #ownerIds}
     * 后续版本中，此字段会被移除
     */
    @Deprecated
    private Long ownerId;

    /**
     * 客户所有者,多个以逗号分隔
     * 作为查询条件时，不会处理空格，按单个整体处理
     */
    private String ownerIds;

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
     * 客户区域标签
     */
    private Integer businessRegionTag;

    /**
     * 销售市场
     */
    private String salesMarket;

    /**
     * 客户别名
     */
    private String alias;

    /**
     * 客户状态 1生效，2禁用..
     * {@link com.dili.customer.sdk.enums.CustomerEnum.State}
     */
    @NotNull(message = "客户状态不能为空", groups = {UpdateView.class})
    private Integer state;

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
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvalTime;

    /**
     * 资料审核备注
     */
    private String approvalNotes;

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
     * 用作查询时，按客户归属部门集进行过滤,多个以逗号隔开
     */
    private Set<Long> departmentIdSet;

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

    /**
     * 获取客户审核状态显示值
     * @return
     */
    public String getApprovalStatusValue() {
        return CustomerEnum.ApprovalStatus.getValueByCode(this.getGrade());
    }

    /**
     * 获取客户状态显示值
     * @return
     */
    public String getStateValue(){
        CustomerEnum.State instance = CustomerEnum.State.getInstance(this.getState());
        if (Objects.nonNull(instance)){
            return instance.getValue();
        }
        return "";
    }

    public void setDepartmentIdSet(Set<Long> departmentIdSet) {
        this.departmentIdSet = departmentIdSet;
        if (!CollectionUtils.isEmpty(departmentIdSet)) {
            this.setMetadata("departmentIdsStr", departmentIdSet.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
    }
}

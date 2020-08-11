package com.dili.customer.sdk.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.sdk.validator.AddView;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
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
     * 客户行业##水果批发/蔬菜批发/超市
     */
    private String profession;

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
     * 经营品类ID,多个逗号分隔
     */
    private String categoryId;

    /**
     * 经营品类名称,多个以逗号隔开
     */
    private String categoryName;

    /**
     * 销售市场
     */
    private String salesMarket;

    /**
     * 客户别名
     */
    private String alias;

    /**
     * 客户类型
     */
    @NotBlank(message = "客户身份不能为空", groups = {AddView.class})
    private String type;

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

package com.dili.customer.sdk.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <B></B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/6/18 15:22
 */
@Data
public class Customer extends BaseDomain {

    private static final long serialVersionUID = -8221228310573544779L;

    private Long id;

    /**
     * 客户编号
     */
    private String code;

    /**
     * 证件号
     */
    private String certificateNumber;

    /**
     * 证件类型
     */
    private String certificateType;

    /**
     * 证件日期##企业时为营业执照日期,如:2011-09-01 至 长期
     */
    private String certificateRange;

    /**
     * 证件是否长期有效 1-是；0-否
     */
    private Integer certificateLongTerm;

    /**
     * 证件地址
     */
    private String certificateAddr;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 出生日期
     */
    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    /**
     * 性别:男,女
     */
    private Integer gender;

    /**
     * 照片
     */
    private String photo;

    /**
     * 联系电话
     */
    private String contactsPhone;

    /**
     * 联系人
     */
    private String contactsName;

    /**
     * 组织类型,个人/企业
     */
    private String organizationType;

    /**
     * 来源系统##外部系统来源标识
     */
    private String sourceSystem;

    /**
     * 来源渠道##租赁业务、系统创建等
     */
    private String sourceChannel;

    /**
     * 注册资金##企业客户属性
     */
    private Long registeredCapital;

    /**
     * 企业员工数
     */
    private String employeeNumber;

    /**
     * 法人证件类型
     */
    private String corporationCertificateType;

    /**
     * 法人证件号
     */
    private String corporationCertificateNumber;

    /**
     * 法人真实姓名
     */
    private String corporationName;

    /**
     * 手机号是否验证
     */
    private Integer isCellphoneValid;

    /**
     * 现住址城市ID path
     * 格式为 100000,110000,1111000
     */
    private String currentCityPath;

    /**
     * 现住址城市名称
     */
    private String currentCityName;

    /**
     * 现住址详细地址
     */
    private String currentAddress;

    /**
     * 创建人
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
     * 修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    /**
     * 是否可用
     */
    private Integer isDelete;

    /**
     * 客户状态 0注销，1生效，2禁用，
     */
    private Integer state;

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
}

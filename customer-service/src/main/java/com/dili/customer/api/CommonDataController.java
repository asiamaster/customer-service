package com.dili.customer.api;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.customer.annotation.UapToken;
import com.dili.customer.commons.service.CommonDataService;
import com.dili.customer.commons.service.DataDictionaryRpcService;
import com.dili.customer.commons.service.DepartmentRpcService;
import com.dili.customer.commons.service.UapUserRpcService;
import com.dili.customer.commons.util.EnumUtil;
import com.dili.customer.domain.dto.UapUserTicket;
import com.dili.customer.sdk.domain.dto.CharacterTypeGroupDto;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.customer.sdk.enums.NationEnum;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.UserQuery;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 客户服务公共数据接口
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/21 16:18
 */
@RestController
@RequestMapping("/api/commonData")
@Slf4j
public class CommonDataController {

    @Autowired
    private CommonDataService commonDataService;
    @Autowired
    private DataDictionaryRpcService dataDictionaryRpcService;
    @Autowired
    private DepartmentRpcService departmentRpcService;
    @Autowired
    private UapUserTicket uapUserTicket;
    @Autowired
    private UapUserRpcService uapUserRpcService;

    /**
     * 查询经营性质(用户类型)
     * @return
     */
    @PostMapping(value = "/listBusinessNature")
    public BaseOutput<List<DataDictionaryValue>> listBusinessNature() {
        return BaseOutput.successData(commonDataService.queryBusinessNature(null));
    }

    /**
     * 获取行业主数据
     * @return
     */
    @PostMapping(value = "/listIndustry")
    public BaseOutput<List<DataDictionaryValue>> listIndustry() {
        return BaseOutput.successData(commonDataService.queryIndustry(null));
    }

    /**
     * 获取个人证件类型
     * @return
     */
    @PostMapping(value = "/listIndividualCertificate")
    public BaseOutput<List<DataDictionaryValue>> listIndividualCertificate() {
        return BaseOutput.successData(commonDataService.queryIndividualCertificate(null));
    }

    /**
     * 获取企业证件类型
     * @return
     */
    @PostMapping(value = "/listEnterpriseCertificate")
    public BaseOutput<List<DataDictionaryValue>> listEnterpriseCertificate() {
        return BaseOutput.successData(commonDataService.queryEnterpriseCertificate(null));
    }

    /**
     * 获取角色主信息
     * @return
     */
    @PostMapping(value = "/listCharacter")
    public BaseOutput listCharacter() {
        return BaseOutput.successData(EnumUtil.toObject(CustomerEnum.CharacterType.class));
    }

    /**
     * 获取市场角色身份分类信息
     * @return
     */
    @PostMapping(value = "/listCharacterSubType")
    public BaseOutput listCharacterSubType(@RequestBody Map<String, Object> params) {
        Object marketId = params.get("marketId");
        Object enable = params.get("enable");
        if (Objects.isNull(marketId)) {
            return BaseOutput.failure("参数丢失").setCode(ResultCode.PARAMS_ERROR);
        }
        Integer state = Boolean.valueOf(Objects.toString(enable, "false")) ? 1 : null;
        List<DataDictionaryValue> dataDictionaryValuesBuyer = dataDictionaryRpcService.listByDdCode(CustomerEnum.CharacterType.买家.getCode(), state, Long.valueOf(marketId.toString()));
        List<DataDictionaryValue> dataDictionaryValuesSeller = dataDictionaryRpcService.listByDdCode(CustomerEnum.CharacterType.经营户.getCode(), state, Long.valueOf(marketId.toString()));
        List<DataDictionaryValue> dataDictionaryValuesOther = dataDictionaryRpcService.listByDdCode(CustomerEnum.CharacterType.其他类型.getCode(), state, Long.valueOf(marketId.toString()));
        List<DataDictionaryValue> allData = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(dataDictionaryValuesBuyer)) {
            dataDictionaryValuesBuyer.forEach(t -> {
                t.setName(CustomerEnum.CharacterType.买家.getValue() + "_" + t.getName());
            });
            allData.addAll(dataDictionaryValuesBuyer);
        }
        if (CollectionUtil.isNotEmpty(dataDictionaryValuesSeller)) {
            dataDictionaryValuesSeller.forEach(t -> {
                t.setName(CustomerEnum.CharacterType.经营户.getValue() + "_" + t.getName());
            });
            allData.addAll(dataDictionaryValuesSeller);
        }
        if (CollectionUtil.isNotEmpty(dataDictionaryValuesOther)) {
            dataDictionaryValuesOther.forEach(t -> {
                t.setName(CustomerEnum.CharacterType.其他类型.getValue() + "_" + t.getName());
            });
            allData.addAll(dataDictionaryValuesOther);
        }
        return BaseOutput.successData(allData);
    }

    /**
     * 获取市场角色分类信息
     * @return
     */
    @PostMapping(value = "/listCharacterTypeGroup")
    public BaseOutput<List<CharacterTypeGroupDto>> listCharacterTypeGroup(@RequestBody Map<String, Object> params) {
        Object marketId = params.get("marketId");
        Object enable = params.get("enable");
        if (Objects.isNull(marketId) || Objects.isNull(enable)) {
            return BaseOutput.failure("参数丢失").setCode(ResultCode.PARAMS_ERROR);
        }
        return BaseOutput.successData(commonDataService.produceCharacterTypeGroup(null, Long.valueOf(marketId.toString()), Boolean.valueOf(enable.toString())));
    }


    /**
     * 获取民族信息
     * @return
     */
    @PostMapping(value = "/listNation")
    public BaseOutput listNation() {
        return BaseOutput.successData(EnumUtil.toObject(NationEnum.class));
    }

    /**
     * 获取客户组织类型
     * @return
     */
    @PostMapping(value = "/listOrganizationType")
    public BaseOutput listOrganizationType() {
        return BaseOutput.successData(EnumUtil.toObject(CustomerEnum.OrganizationType.class));
    }

    /**
     * 获取有权限的部门信息
     * @return
     */
    @UapToken
    @RequestMapping(value = "/listAuthDepartment", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<List<Department>> listAuthDepartment() {
        try {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            Boolean aBoolean = commonDataService.checkCustomerDepartmentAuth(userTicket.getFirmId());
            List<Department> departmentList = departmentRpcService.listData(aBoolean, userTicket.getId(), userTicket.getFirmId());
            return BaseOutput.successData(departmentList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return BaseOutput.failure();
        }
    }

    /**
     * 根据部门获取对应的uap用户信息
     * @return
     */
    @UapToken
    @RequestMapping(value = "/listUapUserByDepIds", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput listUapUserByDepIds(@RequestBody Map<String, Object> params) {
        if (!params.containsKey("departmentIds")) {
            return BaseOutput.failure("部门参数不能为空");
        }
        List<Long> departmentIdList = (List<Long>) params.get("departmentIds");
        if (CollectionUtil.isEmpty(departmentIdList)) {
            return BaseOutput.success();
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        UserQuery userQuery = DTOUtils.newInstance(UserQuery.class);
        userQuery.setFirmCode(userTicket.getFirmCode());
        userQuery.setDepartmentIds(departmentIdList);
        return BaseOutput.successData(uapUserRpcService.listByExample(userQuery));
    }

}

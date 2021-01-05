package com.dili.customer.api;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.customer.commons.service.CommonDataService;
import com.dili.customer.commons.service.DataDictionaryRpcService;
import com.dili.customer.commons.util.EnumUtil;
import com.dili.customer.sdk.domain.dto.CharacterTypeGroupDto;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 客户服务公共数据接口
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/21 16:18
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/commonData")
@Slf4j
public class CommonDataController {

    private final CommonDataService commonDataService;
    private final DataDictionaryRpcService dataDictionaryRpcService;

    /**
     * 查询经营性质(用户类型)
     * @return
     */
    @PostMapping(value = "/listBusinessNature")
    public BaseOutput<List<DataDictionaryValue>> listBusinessNature() {
        return BaseOutput.successData(commonDataService.queryBusinessNature(null));
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
     * 获取市场角色分类信息
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
}

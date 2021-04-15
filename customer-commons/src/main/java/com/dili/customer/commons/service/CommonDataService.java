package com.dili.customer.commons.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.commons.glossary.EnabledStateEnum;
import com.dili.customer.commons.constants.CustomerConstant;
import com.dili.customer.commons.enums.DdCodeEnum;
import com.dili.customer.sdk.domain.CharacterType;
import com.dili.customer.sdk.domain.dto.CharacterSubTypeDto;
import com.dili.customer.sdk.domain.dto.CharacterTypeGroupDto;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 公共数据获取服务类
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/17 21:55
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CommonDataService {

    private final DataDictionaryRpcService dataDictionaryRpcService;
    private final RedisUtil redisUtil;

    /**
     * 获取经营性质数据
     * @param state 状态
     * @return
     */
    public List<DataDictionaryValue> queryBusinessNature(Integer state) {
        return dataDictionaryRpcService.listByDdCode(DdCodeEnum.business_nature.name(), state, null);
    }

    /**
     * 获取客户来源渠道
     * @param state    状态
     * @param marketId 所属市场
     * @return
     */
    public List<DataDictionaryValue> querySourceChannel(Integer state, Long marketId) {
        return dataDictionaryRpcService.listByDdCode(DdCodeEnum.source_channel.name(), state, marketId);
    }

    /**
     * 获取经营行业
     * @param state 状态
     * @return
     */
    public List<DataDictionaryValue> queryIndustry(Integer state) {
        return queryIndustry(state, null);
    }

    /**
     * 获取经营行业
     * @param state 状态
     * @param likeName 名称模糊匹配(由于数据字典不支持模糊查询，所以目前只能在本地循环，字符匹配)
     * @return
     */
    public List<DataDictionaryValue> queryIndustry(Integer state, String likeName) {
        List<DataDictionaryValue> dataDictionaryValueList = dataDictionaryRpcService.listByDdCode(DdCodeEnum.customer_business.name(), state, null);
        if (StrUtil.isNotBlank(likeName)) {
            Pattern p = Pattern.compile(likeName);
            dataDictionaryValueList = dataDictionaryValueList.stream().filter(t -> p.matcher(t.getName()).find()).collect(Collectors.toList());
        }
        return dataDictionaryValueList;
    }

    /**
     * 获取个人证件类型
     * @param state 状态
     * @return
     */
    public List<DataDictionaryValue> queryIndividualCertificate(Integer state) {
        return dataDictionaryRpcService.listByDdCode(DdCodeEnum.individual_certificate.name(), state, null);
    }

    /**
     * 获取企业证件类型
     * @param state 状态
     * @return
     */
    public List<DataDictionaryValue> queryEnterpriseCertificate(Integer state) {
        return dataDictionaryRpcService.listByDdCode(DdCodeEnum.enterprise_certificate.name(), state, null);
    }

    /**
     * 根据市场id，获取并返回当前市场的客户查询，是否按归属部门过滤
     * @param marketId 市场ID
     * @return true-按部门隔离
     */
    public Boolean checkCustomerDepartmentAuth(Long marketId) {
        Optional<DataDictionaryValue> byDdCodeAndCode = dataDictionaryRpcService.getByDdCodeAndCode(DdCodeEnum.customer_data_auth.name(), CustomerConstant.customer_department_auth, EnabledStateEnum.ENABLED.getCode(), marketId);
        return byDdCodeAndCode.isPresent();
    }

    /**
     * 根据市场id，获取并返回当前市场的客户查询，是否按归属人过滤
     * @param marketId 市场ID
     * @return true-按归属人隔离
     */
    public Boolean checkCustomerOwnerAuth(Long marketId) {
        Optional<DataDictionaryValue> byDdCodeAndCode = dataDictionaryRpcService.getByDdCodeAndCode(DdCodeEnum.customer_data_auth.name(), CustomerConstant.customer_owner_auth, EnabledStateEnum.ENABLED.getCode(), marketId);
        return byDdCodeAndCode.isPresent();
    }

    /**
     * 根据市场id，获取并返回当前市场的客户自主完善资料后，是否不用人工审核
     * 由于每个市场只有一个，现在只按市场区分，取其一个
     * @param marketId 市场ID
     * @return true-不需要审核
     */
    public Boolean checkCustomerNotApproval(Long marketId) {
        Optional<DataDictionaryValue> byDdCodeAndCode = dataDictionaryRpcService.getByDdCodeAndCode(DdCodeEnum.customer_data_not_approval.name(), null, EnabledStateEnum.ENABLED.getCode(), marketId);
        return byDdCodeAndCode.isPresent();
    }

    /**
     * 组装生成客户角色身份信息
     * @param characterTypeListData 已有角色分类数据对象
     * @param marketId 所属市场ID
     * @return
     */
    public List<CharacterTypeGroupDto> produceCharacterTypeGroup(List<CharacterType> characterTypeListData, Long marketId) {
        Boolean enable = true;
        if (CollectionUtil.isEmpty(characterTypeListData)) {
            enable = false;
        }
        return produceCharacterTypeGroup(characterTypeListData, marketId, enable);
    }

    /**
     * 组装生成客户角色身份信息
     * @param characterTypeListData 已有角色分类数据对象
     * @param marketId 所属市场ID
     * @param enable 是否只查询启用
     * @return
     */
    public List<CharacterTypeGroupDto> produceCharacterTypeGroup(List<CharacterType> characterTypeListData, Long marketId, Boolean enable) {
        //把已存在的角色类型，转换为角色-List<子类型>数据
        Map<String, List<String>> dataMap = StreamEx.ofNullable(characterTypeListData).flatCollection(Function.identity()).nonNull()
                .filter(t -> StrUtil.isNotBlank(t.getCharacterType())).mapToEntry(item -> item.getCharacterType(), item -> item.getSubType()).grouping();
        Integer state = enable ? 1 : null;
        List<CharacterTypeGroupDto> characterTypeList = new ArrayList<>();
        for (CustomerEnum.CharacterType type : CustomerEnum.CharacterType.values()) {
            CharacterTypeGroupDto dto = new CharacterTypeGroupDto();
            dto.setCode(type.getCode()).setMultiple(type.getMultiple()).setValue(type.getValue());
            Set<String> stringSet = Sets.newHashSet();
            if (dataMap.containsKey(type.getCode())) {
                dto.setSelected(Boolean.TRUE);
                stringSet.addAll(StreamEx.of(dataMap.get(type.getCode())).toSet());
            }
            List<DataDictionaryValue> dataDictionaryValueList = dataDictionaryRpcService.listByDdCode(type.getCode(), state, marketId);
            if (CollectionUtil.isNotEmpty(dataDictionaryValueList)) {
                List<CharacterSubTypeDto> subTypeDtoList = new ArrayList<>();
                dataDictionaryValueList.forEach(t -> {
                    CharacterSubTypeDto subTypeDto = new CharacterSubTypeDto();
                    subTypeDto.setCode(t.getCode());
                    subTypeDto.setName(t.getName());
                    if (stringSet.contains(t.getCode())) {
                        subTypeDto.setSelected(Boolean.TRUE);
                    }
                    subTypeDtoList.add(subTypeDto);
                });
                dto.getSubTypeList().addAll(subTypeDtoList);
            }
            characterTypeList.add(dto);
        }
        return characterTypeList;
    }


    /**
     * 检查手机验证码是否有效
     * @param cellphone 手机号
     * @param sceneCode 验证码场景
     * @param verificationCode 验证码
     * @return 通过返回null，未通过，返回错误信息
     */
    public Optional<String> checkVerificationCode(String cellphone, String sceneCode, String verificationCode) {
        String redisKey = CustomerConstant.REDIS_KEY_PREFIX + "verificationCode:" + sceneCode + ":" + cellphone;
        Object o = redisUtil.get(redisKey);
        if (Objects.isNull(o)) {
            return Optional.of("验证码已失效");
        }
        String number = String.valueOf(o);
        if (verificationCode.equalsIgnoreCase(number)) {
            redisUtil.remove(redisKey);
            return Optional.empty();
        }
        return Optional.of("验证码不正确");
    }
}

package com.dili.customer.commons.service;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.customer.sdk.domain.CharacterType;
import com.dili.customer.sdk.domain.dto.CharacterSubTypeDto;
import com.dili.customer.sdk.domain.dto.CharacterTypeGroupDto;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

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

    /**
     * 获取经营性质数据
     * @param state
     * @param marketId
     * @return
     */
    public List<DataDictionaryValue> queryBusinessNature(Integer state, Long marketId) {
        return dataDictionaryRpcService.listByDdCode("business_nature", state, marketId);
    }



    /**
     * 组装生成客户角色身份信息
     * @param characterTypeListData
     * @param marketId
     * @return
     */
    public List<CharacterTypeGroupDto> produceCharacterTypeGroup(List<CharacterType> characterTypeListData, Long marketId) {
        Map<String, List<String>> dataMap = StreamEx.ofNullable(characterTypeListData).flatCollection(Function.identity()).nonNull()
                .mapToEntry(item -> item.getCharacterType(), item -> item.getSubType()).grouping();
        Integer state = null;
        if (CollectionUtil.isEmpty(characterTypeListData)) {
            state = 1;
        }
        List<CharacterTypeGroupDto> characterTypeList = new ArrayList<>();
        for (CustomerEnum.CharacterType type : CustomerEnum.CharacterType.values()) {
            CharacterTypeGroupDto dto = new CharacterTypeGroupDto();
            dto.setCode(type.getCode()).setMultiple(type.getMultiple()).setValue(type.getValue());
            List<DataDictionaryValue> dataDictionaryValueList = dataDictionaryRpcService.listByDdCode(type.getCode(), state, marketId);
            if (CollectionUtil.isNotEmpty(dataDictionaryValueList)) {
                List<CharacterSubTypeDto> subTypeDtoList = new ArrayList<>();
                Set<String> stringSet = Sets.newHashSet();
                if (dataMap.containsKey(type.getCode())) {
                    dto.setSelected(Boolean.TRUE);
                    stringSet.addAll(StreamEx.of(dataMap.get(type.getCode())).toSet());
                }
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

}

package com.dili.customer.commons.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.customer.commons.constants.CustomerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/17 21:50
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DataDictionaryRpcService {

    private final DataDictionaryRpc dataDictionaryRpc;
    @Resource(name = "caffeineTimedCache")
    private Cache<String, String> caffeineTimedCache;

    /**
     * 根据条件查询数据字典信息
     * @param dataDictionaryValue
     * @return
     */
    public List<DataDictionaryValue> listDataDictionaryValue(DataDictionaryValue dataDictionaryValue) {
        try {
            BaseOutput<List<DataDictionaryValue>> listBaseOutput = dataDictionaryRpc.listDataDictionaryValue(dataDictionaryValue);
            if (Objects.nonNull(listBaseOutput) && listBaseOutput.isSuccess()) {
                return listBaseOutput.getData();
            }
        } catch (Exception e) {
            log.error(String.format("根据条件【%s】查询数据字典异常:%s", JSONUtil.toJsonStr(dataDictionaryValue), e.getMessage()), e);
        }
        return Collections.emptyList();
    }

    /**
     * 根据条件查询数据字典信息
     * @param ddCode 数据字典值
     * @param state  字典值状态 1-启用
     * @param marketId 数据字典值是否按市场隔离，如果为空，则不按市场隔离，否则按传入的具体市场id进行数据查询
     * @return
     */
    public List<DataDictionaryValue> listByDdCode(String ddCode, Integer state, Long marketId) {
        try {
            StringBuilder keyBuilder = new StringBuilder(CustomerConstant.CACHE_KEY).append("_").append(ddCode);
            DataDictionaryValue dataDictionaryValue = DTOUtils.newInstance(DataDictionaryValue.class);
            dataDictionaryValue.setDdCode(ddCode);
            if (Objects.nonNull(state)) {
                dataDictionaryValue.setState(state);
                keyBuilder.append("_").append(state);
            }
            if (Objects.nonNull(marketId)) {
                dataDictionaryValue.setFirmId(marketId);
                keyBuilder.append("_").append(marketId);
            }
            String str = caffeineTimedCache.get(keyBuilder.toString(), t -> {
                BaseOutput<List<DataDictionaryValue>> listBaseOutput = dataDictionaryRpc.listDataDictionaryValue(dataDictionaryValue);
                if (listBaseOutput.isSuccess() && CollectionUtil.isNotEmpty(listBaseOutput.getData())) {
                    return JSONObject.toJSONString(listBaseOutput.getData());
                }
                return null;
            });
            if (StrUtil.isNotBlank(str)) {
                List<DataDictionaryValue> dto = JSONArray.parseArray(str, DataDictionaryValue.class);
                return dto;
            }
        } catch (Exception e) {
            log.error(String.format("根据ddCode【%s】及状态【%s】以及市场【%d】查询数据字典异常:%s", ddCode, state, marketId, e.getMessage()), e);
        }
        return Collections.emptyList();
    }

    /**
     * 根据条件查询数据字典信息
     * @param ddCode 数据字典值
     * @param state  字典值状态 1-启用
     * @param marketId 数据字典值是否按市场隔离，如果为空，则不按市场隔离，否则按传入的具体市场id进行数据查询
     * @return
     */
    public Optional<DataDictionaryValue> getByDdCodeAndCode(String ddCode, String code, Integer state, Long marketId) {
        try {
            StringJoiner keyJoiner = new StringJoiner("_");
            keyJoiner.add(CustomerConstant.CACHE_KEY);
            keyJoiner.add(ddCode);
            keyJoiner.add(code);
            DataDictionaryValue dataDictionaryValue = DTOUtils.newInstance(DataDictionaryValue.class);
            dataDictionaryValue.setDdCode(ddCode);
            dataDictionaryValue.setCode(code);
            if (Objects.nonNull(state)) {
                dataDictionaryValue.setState(state);
                keyJoiner.add(String.valueOf(state));
            }
            if (Objects.nonNull(marketId)) {
                dataDictionaryValue.setFirmId(marketId);
                keyJoiner.add(String.valueOf(marketId));
            }
            String str = caffeineTimedCache.get(keyJoiner.toString(), t -> {
                BaseOutput<List<DataDictionaryValue>> listBaseOutput = dataDictionaryRpc.listDataDictionaryValue(dataDictionaryValue);
                if (listBaseOutput.isSuccess() && CollectionUtil.isNotEmpty(listBaseOutput.getData())) {
                    return JSONObject.toJSONString(listBaseOutput.getData().get(0));
                }
                return null;
            });
            if (StrUtil.isNotBlank(str)) {
                DataDictionaryValue dto = JSONObject.parseObject(str, DataDictionaryValue.class);
                return Optional.ofNullable(dto);
            }
        } catch (Exception e) {
            log.error(String.format("根据ddCode【%s】及状态【%s】以及市场【%d】查询数据字典异常:%s", ddCode, state, marketId, e.getMessage()), e);
        }
        return Optional.empty();
    }
}

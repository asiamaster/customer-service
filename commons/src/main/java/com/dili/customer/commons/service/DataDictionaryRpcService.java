package com.dili.customer.commons.service;

import cn.hutool.json.JSONUtil;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
            DataDictionaryValue dataDictionaryValue = DTOUtils.newInstance(DataDictionaryValue.class);
            dataDictionaryValue.setDdCode(ddCode);
            if (Objects.nonNull(state)) {
                dataDictionaryValue.setState(state);
            }
            if (Objects.nonNull(marketId)) {
                dataDictionaryValue.setFirmId(marketId);
            }
            BaseOutput<List<DataDictionaryValue>> listBaseOutput = dataDictionaryRpc.listDataDictionaryValue(dataDictionaryValue);
            if (Objects.nonNull(listBaseOutput) && listBaseOutput.isSuccess()) {
                return listBaseOutput.getData();
            }
        } catch (Exception e) {
            log.error(String.format("根据ddCode【%s】及状态【%s】以及市场【%d】查询数据字典异常:%s", ddCode, state, marketId, e.getMessage()), e);
        }
        return Collections.emptyList();
    }
}

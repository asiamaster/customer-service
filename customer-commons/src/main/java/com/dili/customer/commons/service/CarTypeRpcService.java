package com.dili.customer.commons.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.dili.assets.sdk.dto.CarTypeDTO;
import com.dili.assets.sdk.dto.CarTypeForBusinessDTO;
import com.dili.assets.sdk.rpc.CarTypeRpc;
import com.dili.customer.commons.constants.CustomerConstant;
import com.dili.ss.domain.BaseOutput;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/22 9:47
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CarTypeRpcService {

    private final CarTypeRpc carTypeRpc;

    @Resource(name = "caffeineTimedCache")
    private Cache<String, String> caffeineTimedCache;

    /**
     * 获取市场车型
     * @return
     */
    public List<CarTypeDTO> listCarType() {
        try {
            String cacheKey = CustomerConstant.CACHE_KEY + "_carType";
            String str = caffeineTimedCache.get(cacheKey, t -> {
                BaseOutput<List<CarTypeDTO>> listBaseOutput = carTypeRpc.listCarType();
                if (listBaseOutput.isSuccess() && CollectionUtil.isNotEmpty(listBaseOutput.getData())) {
                    return JSONUtil.toJsonStr(listBaseOutput.getData());
                }
                return null;
            });
            if (StrUtil.isNotBlank(str)) {
                List<CarTypeDTO> dto = JSONUtil.toList(str, CarTypeDTO.class);
                return dto;
            }
            return Collections.emptyList();
        } catch (Exception e) {
            log.error(String.format("获取市场:%d 车型信息异常:%s", e.getMessage()), e);
        }
        return Collections.emptyList();
    }

    /**
     * 获取车型，并按id转换为map结构
     * @return
     */
    public Map<Long, CarTypeDTO> listMapInfo() {
        List<CarTypeDTO> carTypeDTOS = this.listCarType();
        if (CollectionUtil.isNotEmpty(carTypeDTOS)) {
            return StreamEx.of(carTypeDTOS).toMap(CarTypeDTO::getId, Function.identity());
        }
        return Collections.emptyMap();
    }

    public List<CarTypeDTO> listCarTypes() {
        try {
            String cacheKey = CustomerConstant.CACHE_KEY + "_carType";
            String str = caffeineTimedCache.get(cacheKey, t -> {
                BaseOutput<List<CarTypeDTO>> listBaseOutput = carTypeRpc.listCarType();
                if (listBaseOutput.isSuccess() && CollectionUtil.isNotEmpty(listBaseOutput.getData())) {
                    return JSONUtil.toJsonStr(listBaseOutput.getData());
                }
                return null;
            });
            if (StrUtil.isNotBlank(str)) {
                List<CarTypeDTO> dto = JSONUtil.toList(str, CarTypeDTO.class);
                return dto;
            }
            return Collections.emptyList();
        } catch (Exception e) {
            log.error(String.format("获取市场:%d 车型信息异常:%s", e.getMessage()), e);
        }
        return Collections.emptyList();
    }
}

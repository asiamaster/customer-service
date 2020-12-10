package com.dili.customer.service.remote;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.dili.customer.constants.CustomerServiceConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.dto.FirmDto;
import com.dili.uap.sdk.rpc.FirmRpc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/10/16 15:11
 */
@RequiredArgsConstructor
@Service
public class MarketRpcService {

    private final FirmRpc firmRpc;
    private final RedisUtil redisUtil;

    /**
     * 查询市场信息，并保存到map中
     * @return
     */
    public Map<String, String> listForMap() {
        String redisKey = CustomerServiceConstant.REDIS_KEY_PREFIX + "firmMap";
        Object o = redisUtil.get(redisKey);
        if (Objects.nonNull(o)) {
            return (Map<String, String>) JSONUtil.parse(o);
        } else {
            BaseOutput<List<Firm>> output = firmRpc.listByExample(DTOUtils.newInstance(FirmDto.class));
            if (output.isSuccess() && CollectionUtil.isNotEmpty(output.getData())) {
                Map<String, String> collect = output.getData().stream().collect(Collectors.toMap(t -> String.valueOf(t.getId()), t -> t.getName()));
                redisUtil.set(redisKey, collect, 1L, TimeUnit.DAYS);
                return collect;
            }
            return Collections.emptyMap();
        }
    }
}

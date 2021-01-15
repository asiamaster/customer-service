package com.dili.customer.commons.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.customer.commons.constants.CustomerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.UserDataAuth;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.FirmDto;
import com.dili.uap.sdk.glossary.DataAuthType;
import com.dili.uap.sdk.rpc.DataAuthRpc;
import com.dili.uap.sdk.rpc.FirmRpc;
import com.dili.uap.sdk.session.SessionContext;
import com.github.benmanes.caffeine.cache.Cache;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/17 16:44
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MarketRpcService {

    private final FirmRpc firmRpc;
    private final RedisUtil redisUtil;
    private final DataAuthRpc dataAuthRpc;
    @Resource(name = "caffeineMaxSizeCache")
    private Cache<String, String> caffeineMaxSizeCache;


    /**
     * 查询市场信息，并保存到map中
     * @return
     */
    public Map<String, String> listForMap() {
        String redisKey = CustomerConstant.REDIS_KEY_PREFIX + "firmMap";
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

    /**
     * 根据条件查询市场
     * @param firm
     * @author wangmi
     * @return
     */
    public List<Firm> listByExample(FirmDto firm){
        BaseOutput<List<Firm>> output = firmRpc.listByExample(firm);
        return output.isSuccess() ? output.getData() : null;
    }

    /**
     * 当前用户拥有访问权限的firmId
     * @return
     */
    public Set<Long> getCurrentUserFirmIds() {
        return getCurrentUserFirmIds(null);
    }

    /**
     * 当前用户拥有访问权限的firmId集
     * @param userId 用户ID
     * @return
     */
    public Set<Long> getCurrentUserFirmIds(Long userId) {
        List<Firm> list = this.getCurrentUserFirms(userId);
        Set<Long> resultSet = list.stream().distinct().map(Firm::getId).collect(Collectors.toSet());
        return resultSet;
    }

    /**
     * 当前用户拥有访问权限的firmId
     * @param firmId 市场ID
     * @return
     */
    public Set<Long> getCurrentUserAvaliableFirmIds(Long firmId) {
        Set<Long> resultSet = this.getCurrentUserFirmIds(null);
        if (Objects.isNull(firmId) || !resultSet.contains(firmId)) {
            return resultSet;
        } else {
            return Sets.newHashSet(firmId);
        }
    }

    /**
     * 通过id查询firm
     * @param firmId
     * @return
     */
    public Optional<Firm> getFirmById(Long firmId) {
        if (Objects.isNull(firmId)) {
            return Optional.empty();
        }
        try {
            StringBuilder keyBuilder = new StringBuilder(CustomerConstant.CACHE_KEY).append("_market_").append(firmId);
            String str = caffeineMaxSizeCache.get(keyBuilder.toString(), t -> {
                BaseOutput<Firm> baseOutput = firmRpc.getById(firmId);
                if (baseOutput.isSuccess() && Objects.nonNull(baseOutput.getData())) {
                    return JSONObject.toJSONString(baseOutput.getData());
                }
                return null;
            });
            if (StrUtil.isNotBlank(str)) {
                Firm firm = JSONObject.parseObject(str, Firm.class);
                return Optional.ofNullable(firm);
            }
        } catch (Exception e) {
            log.error(String.format("根据id【%s】查询市场信息异常:%s", firmId, e.getMessage()), e);
        }
        return Optional.empty();
    }

    /**
     * 获得当前用户拥有的所有Firm
     * @return
     */
    public List<Firm> getCurrentUserFirms() {
        return getCurrentUserFirms(null);
    }

    /**
     * 获得当前用户拥有的所有Firm
     * @param userId 用户ID，如果为空，则从session中获取，如果未获取到，返回空
     * @return
     */
    public List<Firm> getCurrentUserFirms(Long userId) {
        UserDataAuth userDataAuth = DTOUtils.newDTO(UserDataAuth.class);
        if (null == userId) {
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            if (null == userTicket){
                return Collections.emptyList();
            }
            userDataAuth.setUserId(SessionContext.getSessionContext().getUserTicket().getId());
        } else {
            userDataAuth.setUserId(userId);
        }
        userDataAuth.setRefCode(DataAuthType.MARKET.getCode());
        BaseOutput<List<Map>> out = dataAuthRpc.listUserDataAuthDetail(userDataAuth);
        if (out.isSuccess() && CollectionUtil.isNotEmpty(out.getData())) {
            List<String> firmCodeList = (List<String>) out.getData().stream().flatMap(m -> m.keySet().stream()).collect(Collectors.toList());
            FirmDto firmDto = DTOUtils.newInstance(FirmDto.class);
            firmDto.setCodes(firmCodeList);
            BaseOutput<List<Firm>> listBaseOutput = firmRpc.listByExample(firmDto);
            if (listBaseOutput.isSuccess() && CollectionUtil.isNotEmpty(listBaseOutput.getData())) {
                return listBaseOutput.getData();
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }
}

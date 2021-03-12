package com.dili.customer.commons.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.customer.commons.constants.CustomerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.UserQuery;
import com.dili.uap.sdk.rpc.UserRpc;
import com.dili.uap.sdk.session.SessionContext;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/14 17:44
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UapUserRpcService {

    private final UserRpc userRpc;
    private final MarketRpcService marketRpcService;

    @Resource(name = "caffeineTimedCache")
    private Cache<String, String> caffeineTimedCache;

    /**
     * 根据条件查询用户信息
     * @param userQuery
     * @return
     */
    public List<User> listByExample(UserQuery userQuery) {
        try {
            BaseOutput<List<User>> baseOutput = userRpc.listByExample(userQuery);
            return baseOutput.isSuccess() ? baseOutput.getData() : Collections.EMPTY_LIST;
        } catch (Exception e) {
            log.error(String.format("根据条件:%s 查询UAP用户信息异常:%s", JSONUtil.toJsonStr(userQuery), e.getMessage()), e);
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * 根据姓名模糊获取当前用户有权限的市场中的对应用户
     * @param realName 真实姓名
     * @return
     */
    public List<User> getCurrentAuthMarketUsers(String realName){
        List<Firm> firms = marketRpcService.getCurrentUserFirms();
        if (CollectionUtil.isNotEmpty(firms)){
            UserQuery condition = DTOUtils.newInstance(UserQuery.class);
            condition.setFirmCodes(firms.stream().distinct().map(Firm::getCode).collect(Collectors.toList()));
            if (StrUtil.isNotBlank(realName)) {
                condition.setRealName(realName);
            }
            return listByExample(condition);
        }
        return Collections.emptyList();
    }

    /**
     * 根据真实姓名模糊获取当前市场的用户信息
     * @param realName 真实姓名
     * @return
     */
    public List<User> getCurrentMarketUser(String realName) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (null == userTicket) {
            return Collections.emptyList();
        }
        UserQuery condition = DTOUtils.newInstance(UserQuery.class);
        condition.setFirmCode(userTicket.getFirmCode());
        if (StrUtil.isNotBlank(realName)) {
            condition.setRealName(realName);
        }
        return listByExample(condition);
    }

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return
     */
    public Optional<User> getUserById(Long userId) {
        if (Objects.isNull(userId)) {
            return Optional.empty();
        }

        try {
            StringBuilder keyBuilder = new StringBuilder(CustomerConstant.CACHE_KEY).append("_uapUser_").append(userId);
            String str = caffeineTimedCache.get(keyBuilder.toString(), t -> {
                BaseOutput<User> baseOutput = userRpc.get(userId);
                if (baseOutput.isSuccess() && Objects.nonNull(baseOutput.getData())) {
                    return JSONObject.toJSONString(baseOutput.getData());
                }
                return null;
            });
            if (StrUtil.isNotBlank(str)) {
                User dto = JSONObject.parseObject(str, User.class);
                return Optional.ofNullable(dto);
            }
        } catch (Exception e) {
            log.error(String.format("根据id【%s】查询uapUser异常:%s", userId, e.getMessage()), e);
        }
        return Optional.empty();
    }

    /**
     * 根据用户ID集批量获取用户信息
     * @param ids id集合
     * @return
     */
    public List<User> listUserByIds(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }
        BaseOutput<List<User>> baseOutput = userRpc.listUserByIds(ids);
        return baseOutput.isSuccess() ? baseOutput.getData() : Collections.emptyList();
    }
}

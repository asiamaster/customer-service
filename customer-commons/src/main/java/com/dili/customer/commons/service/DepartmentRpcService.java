package com.dili.customer.commons.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dili.customer.commons.constants.CustomerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.dto.DepartmentDto;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/15 10:31
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentRpcService {

    private final DepartmentRpc departmentRpc;

    @Resource(name = "caffeineTimedCache")
    private Cache<String, String> caffeineTimedCache;

    /**
     * 查询部门信息
     * @param departmentDto 查询条件
     * @return 部门信息结果集
     */
    public List<Department> listByExample(DepartmentDto departmentDto) {
        try {
            BaseOutput<List<Department>> baseOutput = departmentRpc.listByExample(departmentDto);
            if (baseOutput.isSuccess()) {
                return baseOutput.getData();
            }
            log.warn(String.format("根据条件【%s】查询部门信息返回失败:%s", JSONUtil.toJsonStr(departmentDto), JSONUtil.toJsonStr(baseOutput)));
            return Collections.emptyList();
        } catch (Exception e) {
            log.error(String.format("根据条件【%s】查询部门异常:%s", JSONUtil.toJsonStr(departmentDto), e.getMessage()), e);
            return Collections.emptyList();
        }
    }

    /**
     * 根据市场ID查询部门信息
     * @param marketId 市场ID
     * @return 部门信息结果集
     */
    public List<Department> listByMarketId(Long marketId) {
        if (Objects.isNull(marketId)) {
            return Collections.emptyList();
        }
        try {
            StringBuilder keyBuilder = new StringBuilder(CustomerConstant.CACHE_KEY).append("_department_").append(marketId);
            String str = caffeineTimedCache.get(keyBuilder.toString(), t -> {
                DepartmentDto departmentDto = DTOUtils.newInstance(DepartmentDto.class);
                departmentDto.setFirmId(marketId);
                BaseOutput<List<Department>> baseOutput = departmentRpc.listByExample(departmentDto);
                if (baseOutput.isSuccess() && CollectionUtil.isNotEmpty(baseOutput.getData())) {
                    return JSONObject.toJSONString(baseOutput.getData());
                }
                return null;
            });
            if (StrUtil.isNotBlank(str)) {
                List<Department> dto = JSONArray.parseArray(str, Department.class);
                return dto;
            }
        } catch (Exception e) {
            log.error(String.format("根据市场【%s】查询部门异常:%s", marketId, e.getMessage()), e);
        }
        return Collections.emptyList();
    }

    /**
     * 根据市场ID查询部门信息
     * @param id 部门ID
     * @return 部门信息结果集
     */
    public Optional<Department> getById(Long id) {
        if (Objects.isNull(id)) {
            return Optional.empty();
        }
        try {
            StringBuilder keyBuilder = new StringBuilder(CustomerConstant.CACHE_KEY).append("_department_").append(id);
            String str = caffeineTimedCache.get(keyBuilder.toString(), t -> {
                BaseOutput<Department> baseOutput = departmentRpc.get(id);
                if (baseOutput.isSuccess() && Objects.nonNull(baseOutput.getData())) {
                    return JSONObject.toJSONString(baseOutput.getData());
                }
                return null;
            });
            if (StrUtil.isNotBlank(str)) {
                Department dto = JSONObject.parseObject(str, Department.class);
                return Optional.ofNullable(dto);
            }
        } catch (Exception e) {
            log.error(String.format("根据id【%s】查询部门异常:%s", id, e.getMessage()), e);
        }
        return Optional.empty();
    }


    /**
     * 根据部门ID信息
     * @param idSet 部门ID集
     * @return 部门信息结果集
     */
    public List<Department> getByIds(Set<Long> idSet) {
        if (CollectionUtil.isEmpty(idSet)) {
            return Collections.emptyList();
        }
        try {
            DepartmentDto departmentDto = DTOUtils.newInstance(DepartmentDto.class);
            departmentDto.setIds(idSet.stream().map(t -> String.valueOf(t)).collect(Collectors.toList()));
            BaseOutput<List<Department>> baseOutput = departmentRpc.listByExample(departmentDto);
            if (baseOutput.isSuccess()) {
                return baseOutput.getData();
            } else {
                log.error(String.format("根据id集【%s】查询部门失败:%s", StrUtil.join(",", idSet), baseOutput.getMessage()));
            }
        } catch (Exception e) {
            log.error(String.format("根据id集【%s】查询部门异常:%s", StrUtil.join(",", idSet), e.getMessage()), e);
        }
        return Collections.emptyList();
    }

    /**
     * 根据用户ID及市场ID，查询用户在某市场中有权限的部门
     * @param userId 用户ID
     * @param marketId 市场ID
     * @return
     */
    public List<Department> listUserAuthDepartmentByFirmId(Long userId, Long marketId) {
        if (Objects.isNull(marketId) || Objects.isNull(userId)) {
            return Collections.emptyList();
        }
        try {
            StringJoiner keyBuilder = new StringJoiner("_");
            keyBuilder.add(CustomerConstant.CACHE_KEY);
            keyBuilder.add("authDepartment");
            keyBuilder.add(String.valueOf(userId));
            keyBuilder.add(String.valueOf(marketId));
            String str = caffeineTimedCache.get(keyBuilder.toString(), t -> {
                BaseOutput<List<Department>> baseOutput = departmentRpc.listUserAuthDepartmentByFirmId(userId, marketId);
                if (baseOutput.isSuccess() && CollectionUtil.isNotEmpty(baseOutput.getData())) {
                    return JSONObject.toJSONString(baseOutput.getData());
                }
                return null;
            });
            if (StrUtil.isNotBlank(str)) {
                List<Department> dto = JSONArray.parseArray(str, Department.class);
                return dto;
            }
        } catch (Exception e) {
            log.error(String.format("根据用户【%d】市场【%d】查询权限部门异常:%s", userId, marketId, e.getMessage()), e);
        }
        return Collections.emptyList();
    }

    /**
     * 查询部门信息，如果查询权限部门，userId为必须，如果查询市场所有部门，则userId 可不传
     * @param auth 是否查询用户的权限部门，true-是
     * @param userId 用户ID
     * @param marketId 市场ID
     * @return
     */
    public List<Department> listData(Boolean auth, Long userId, Long marketId) {
        if (Objects.isNull(marketId)) {
            return Collections.emptyList();
        }
        if (auth) {
            if (Objects.isNull(userId)) {
                return Collections.emptyList();
            }
            return listUserAuthDepartmentByFirmId(userId, marketId);
        } else {
            return listByMarketId(marketId);
        }
    }

    /**
     * 查询部门信息，如果查询权限部门，userId为必须，如果查询市场所有部门，则userId 可不传
     *
     * @param auth     是否查询用户的权限部门，true-是
     * @param keyword  部门名称关键字
     * @param userId   用户ID
     * @param marketId 市场ID
     * @return
     */
    public List<Department> listDataWithKeyWord(String keyword, Boolean auth, Long userId, Long marketId) {
        if (Objects.isNull(marketId)) {
            return Collections.emptyList();
        }
        if (auth) {
            if (Objects.isNull(userId)) {
                return Collections.emptyList();
            }
            if (Objects.isNull(marketId) || Objects.isNull(userId)) {
                return Collections.emptyList();
            }
            try {
                StringJoiner keyBuilder = new StringJoiner("_");
                keyBuilder.add(CustomerConstant.CACHE_KEY);
                keyBuilder.add("authDepartment");
                keyBuilder.add(String.valueOf(userId));
                keyBuilder.add(String.valueOf(marketId));
                String str = caffeineTimedCache.get(keyBuilder.toString(), t -> {
                    BaseOutput<List<Department>> baseOutput = departmentRpc.listUserAuthDepartmentByFirmId(userId, marketId);
                    if (baseOutput.isSuccess() && CollectionUtil.isNotEmpty(baseOutput.getData())) {
                        return JSONObject.toJSONString(baseOutput.getData());
                    }
                    return null;
                });
                if (StrUtil.isNotBlank(str)) {
                    List<Department> departments = JSONArray.parseArray(str, Department.class);
                    return filterByNameLike(departments, keyword);
                }
            } catch (Exception e) {
                log.error(String.format("根据用户【%d】市场【%d】查询权限部门异常:%s", userId, marketId, e.getMessage()), e);
            }
            return Collections.emptyList();
        } else {
            List<Department> departments = listByMarketId(marketId);
            return filterByNameLike(departments, keyword);
        }
    }

    private List<Department> filterByNameLike(List<Department> departments, String keyword) {
        return departments.stream().filter(d -> d.getName().contains(keyword)).collect(Collectors.toList());
    }


}

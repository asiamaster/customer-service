package com.dili.customer.commons.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        BaseOutput<List<Department>> baseOutput = departmentRpc.listByExample(departmentDto);
        return baseOutput.isSuccess() ? baseOutput.getData() : null;
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


}

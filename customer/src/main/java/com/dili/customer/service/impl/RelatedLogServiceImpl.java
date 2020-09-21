/*
*  Copyright 2019-2020 ShaoFan
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package com.dili.customer.service.impl;

import com.dili.customer.domain.RelatedLog;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.dili.customer.service.RelatedLogService;
import com.dili.customer.sdk.dto.RelatedLogQuery;
import com.dili.customer.mapper.RelatedLogMapper;
import org.springframework.stereotype.Service;
import com.dili.ss.base.BaseServiceImpl;
import java.util.List;

/**
* @website http://shaofan.org
* @description 服务实现
* @author shaofan
* @date 2020-09-07
**/
@Service
@RequiredArgsConstructor
public class RelatedLogServiceImpl extends BaseServiceImpl<RelatedLog, Long> implements RelatedLogService {

    private final RelatedLogMapper relatedLogMapper;

    @Override
    public PageInfo<RelatedLog> queryAll(RelatedLogQuery query){
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<RelatedLog> list = relatedLogMapper.selectByQuery(query);
        PageInfo<RelatedLog> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            relatedLogMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public void enableAll(Long[] ids) {
        for (Long id : ids) {
            System.out.println("自行实现批量启用");
        }
    }

    @Override
    public void disableAll(Long[] ids) {
        for (Long id : ids) {
            System.out.println("自行实现批量禁用");
        }
    }

    @Override
    public BaseOutput saveRelatedLog(RelatedLog relatedLog) {
        this.saveOrUpdate(relatedLog);
        return BaseOutput.success();
    }

    @Override
    public BaseOutput updateRelatedLog(RelatedLog relatedLog) {
        this.updateSelective(relatedLog);
        return BaseOutput.success();
    }
}
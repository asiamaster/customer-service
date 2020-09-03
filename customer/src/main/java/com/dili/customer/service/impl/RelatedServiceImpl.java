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

import com.dili.customer.domain.Related;
import com.dili.customer.domain.dto.RelatedList;
import com.dili.customer.sdk.dto.RelatedQuery;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.dili.customer.service.RelatedService;
import com.dili.customer.mapper.RelatedMapper;
import org.springframework.stereotype.Service;
import com.dili.ss.base.BaseServiceImpl;

import java.util.List;

/**
 * @author shaofan
 * @website http://shaofan.org
 * @description 服务实现
 * @date 2020-08-24
 **/
@Service
@RequiredArgsConstructor
public class RelatedServiceImpl extends BaseServiceImpl<Related, Long> implements RelatedService {

    private final RelatedMapper relatedMapper;

    @Override
    public PageInfo<RelatedList> queryAll(RelatedQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<RelatedList> list = relatedMapper.selectByQuery(query);
        PageInfo<RelatedList> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public void deleteAll(Long parent) {
        Related example = new Related();
        example.setParent(parent);
        this.deleteByExample(example);
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
    public BaseOutput saveRelated(Related related) {
        this.saveOrUpdate(related);
        return BaseOutput.success();
    }

    @Override
    public BaseOutput updateRelated(Related related) {
        this.updateSelective(related);
        return BaseOutput.success();
    }
}
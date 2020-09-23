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
package com.dili.customer.controller;

import com.dili.commons.bstable.TableResult;
import com.dili.customer.domain.Related;
import com.dili.customer.domain.dto.RelatedList;
import com.dili.customer.sdk.dto.RelatedDto;
import com.dili.customer.service.RelatedService;
import com.dili.customer.sdk.dto.RelatedQuery;
import lombok.RequiredArgsConstructor;
import com.dili.ss.domain.BaseOutput;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author shaofan
 * @website http://shaofan.org
 * @date 2020-08-24
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/related")
public class RelatedController {

    private final RelatedService relatedService;

    /**
     * 查询数据分页
     *
     * @param query    条件
     * @param pageable 分页参数
     * @return PageInfo<Related>
     */
    @PostMapping("/query")
    public Object query(@RequestBody RelatedQuery query) {
        PageInfo<RelatedList> page = relatedService.queryAll(query);
        TableResult<RelatedList> result = new TableResult<>(page.getPageNum(), page.getTotal(), page.getList());
        return result;
    }

    /**
     * 查询数据分页
     */
    @PostMapping("/getParent")
    public Object getParent(@RequestBody RelatedQuery query) {
        Related related = new Related();
        if (query.getCustomerId() == null || query.getMarketId() == null) {
            return BaseOutput.failure("参数不能为空");
        }
        related.setCustomerId(query.getCustomerId());
        related.setMarketId(query.getMarketId());
        List<Related> relateds = relatedService.listByExample(related);
        return BaseOutput.successData(relateds);
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Object add(@RequestBody Related related) {
        relatedService.saveOrUpdate(related);
        return BaseOutput.success();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public Object update(@RequestBody Related related) {
        relatedService.updateSelective(related);
        return BaseOutput.success();
    }

    /**
     * listByExample
     *
     * @param related
     * @return
     */
    @PostMapping("/listByExample")
    public List<Related> listByExample(@RequestBody Related related) {
        return relatedService.listByExample(related);
    }

    /**
     * 获取
     */
    @PostMapping("/get")
    public BaseOutput<RelatedDto> get(@RequestBody Long id) {
        return BaseOutput.success().setData(relatedService.get(id));
    }

    /**
     * 多选删除
     */
    @PostMapping("/deleteAll")
    public BaseOutput deleteAll(@RequestBody Long parent) {
        relatedService.deleteAll(parent);
        return BaseOutput.success();
    }

    /**
     * 多选启用
     */
    @PostMapping("/enableAll")
    public BaseOutput enableAll(@RequestBody Long[] ids) {
        relatedService.enableAll(ids);
        return BaseOutput.success();
    }

    /**
     * 多选禁用
     */
    @PostMapping("/disableAll")
    public BaseOutput disableAll(@RequestBody Long[] ids) {
        relatedService.disableAll(ids);
        return BaseOutput.success();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public BaseOutput delete(@RequestBody Long id) {
        relatedService.delete(id);
        return BaseOutput.success();
    }
}
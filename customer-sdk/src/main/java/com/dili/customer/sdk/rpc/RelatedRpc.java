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
package com.dili.customer.sdk.rpc;

import com.dili.commons.bstable.TableResult;
import com.dili.customer.sdk.dto.RelatedDto;
import com.dili.customer.sdk.dto.RelatedList;
import com.dili.customer.sdk.dto.RelatedQuery;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
* @website http://shaofan.org
* @author shaofan
* @date 2020-08-24
**/
@FeignClient(name = "customer-service", contextId = "relatedRpc", url = "${customerService.url:}")
public interface RelatedRpc {

    /**
    * 查询关联客户数据分页
    */
    @RequestMapping(value = "/api/related/query", method = RequestMethod.POST)
    TableResult<RelatedList> query(RelatedQuery query);

    /**
     * 查询关联客户数据分页
     */
    @RequestMapping(value = "/api/related/getParent", method = RequestMethod.POST)
    BaseOutput<List<RelatedDto>> getParent(RelatedQuery query);

    /**
    * 新增关联客户
    */
    @RequestMapping(value = "/api/related/add", method = RequestMethod.POST)
    BaseOutput add(RelatedDto dto);

    /**
    * 修改关联客户
    */
    @RequestMapping(value = "/api/related/update", method = RequestMethod.POST)
    BaseOutput update(RelatedDto dto);

    /**
    * 多选删除关联客户
    */
    @RequestMapping(value = "/api/related/deleteAll", method = RequestMethod.POST)
    BaseOutput deleteAll(Long parentId);

    /**
    * 多选启用关联客户
    */
    @RequestMapping(value = "/api/related/enableAll", method = RequestMethod.POST)
    BaseOutput enableAll(Long[] ids);

    /**
    * 多选禁用关联客户
    */
    @RequestMapping(value = "/api/related/disableAll", method = RequestMethod.POST)
    BaseOutput disableAll(Long[] ids);

    /**
    * 获取关联客户
    */
    @RequestMapping(value = "/api/related/get", method = RequestMethod.POST)
    BaseOutput<RelatedDto> get(Long id);

    /**
     * listByExample
     */
    @RequestMapping(value = "/api/related/listByExample", method = RequestMethod.POST)
    List<RelatedDto> listByExample(RelatedDto dto);

    /**
     * 删除关联客户
     */
    @RequestMapping(value = "/api/related/delete", method = RequestMethod.POST)
    BaseOutput delete(Long parentId);

}
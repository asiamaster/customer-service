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
import com.dili.customer.sdk.dto.RelatedLogDto;
import com.dili.customer.sdk.dto.RelatedLogQuery;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
* @website http://shaofan.org
* @author shaofan
* @date 2020-09-07
**/
@FeignClient(name = "customer-service", contextId = "relatedLogRpc", url = "${customerManageService.url:}")
public interface RelatedLogRpc {

    /**
    * 查询关联客户日志数据分页
    */
    @RequestMapping(value = "/api/relatedLog/query", method = RequestMethod.POST)
    TableResult<RelatedLogDto> query(RelatedLogQuery query);

    /**
    * 新增关联客户日志
    */
    @RequestMapping(value = "/api/relatedLog/add", method = RequestMethod.POST)
    BaseOutput add(RelatedLogDto dto);

    /**
    * 修改关联客户日志
    */
    @RequestMapping(value = "/api/relatedLog/update", method = RequestMethod.POST)
    BaseOutput update(RelatedLogDto dto);

    /**
    * 多选删除关联客户日志
    */
    @RequestMapping(value = "/api/relatedLog/deleteAll", method = RequestMethod.POST)
    BaseOutput deleteAll(Long[] ids);

    /**
    * 多选启用关联客户日志
    */
    @RequestMapping(value = "/api/relatedLog/enableAll", method = RequestMethod.POST)
    BaseOutput enableAll(Long[] ids);

    /**
    * 多选禁用关联客户日志
    */
    @RequestMapping(value = "/api/relatedLog/disableAll", method = RequestMethod.POST)
    BaseOutput disableAll(Long[] ids);

    /**
    * 获取关联客户日志
    */
    @RequestMapping(value = "/api/relatedLog/get", method = RequestMethod.POST)
    BaseOutput<RelatedLogDto> get(Long id);

}
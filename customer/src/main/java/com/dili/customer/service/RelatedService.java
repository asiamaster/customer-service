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
package com.dili.customer.service;

import com.dili.customer.domain.Related;
import com.dili.customer.domain.dto.RelatedList;
import com.dili.customer.sdk.dto.RelatedQuery;
import com.dili.ss.domain.BaseOutput;
import com.github.pagehelper.PageInfo;
import com.dili.ss.base.BaseService;
import java.util.List;

/**
* @website http://shaofan.org
* @description 服务接口
* @author shaofan
* @date 2020-08-24
**/
public interface RelatedService extends BaseService<Related, Long> {

    /**
    * 查询数据分页
    * @param query 条件
    * @return PageInfo<Related>
    */
    PageInfo<RelatedList> queryAll(RelatedQuery query);


    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Long parent);

    /**
    * 多选启用
    * @param ids /
    */
    void enableAll(Long[] ids);

    /**
    * 多选禁用
    * @param ids /
    */
    void disableAll(Long[] ids);

    /**
    *  新增
    * @param tradeType
    * @return
    */
    BaseOutput saveRelated(Related related);

    /**
    *  修改
    * @param tradeType
    * @return
    */
    BaseOutput updateRelated(Related related);

}
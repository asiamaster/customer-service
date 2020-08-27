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
package com.dili.customer.sdk.dto;

import lombok.Data;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
* @website http://shaofan.org
* @description /
* @author shaofan
* @date 2020-08-24
**/
@Data
public class RelatedDto extends BaseDomain implements Serializable {

    /** 主键 */
    private Long id;

    /** 客户 */
    private Long customerId;

    /** 父客户 */
    private Long parent;

    /** 市场id */
    private Long marketId;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    /** 关联时间开始 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime relatedTimeStart;

    /** 关联时间结束 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime relatedTimeEnd;

    /** 关联商品 */
    private String category;
}
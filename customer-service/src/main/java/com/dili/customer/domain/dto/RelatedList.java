
package com.dili.customer.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.annotation.Generated;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class RelatedList {

    private String cardNo;
    private String code;
    private Integer customerCount;
    private String departmentIds;
    private String customerContactsPhone;
    private Long customerId;
    private Long marketId;
    private String customerName;
    private List<Long> goods;
    private Boolean popover;
    private List<String> times;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

}

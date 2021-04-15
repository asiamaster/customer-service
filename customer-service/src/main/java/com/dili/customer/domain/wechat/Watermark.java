package com.dili.customer.domain.wechat;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/10 17:21
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Watermark {

    private String timestamp;

    @SerializedName("appid")
    private String appId;
}

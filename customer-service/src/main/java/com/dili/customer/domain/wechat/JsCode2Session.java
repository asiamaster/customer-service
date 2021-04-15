package com.dili.customer.domain.wechat;

import com.dili.customer.utils.GsonBuilderUtil;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/10 16:34
 */
@Data
public class JsCode2Session {

    /**
     * 会话密钥
     *
     */
    @SerializedName("session_key")
    private String sessionKey;
    /**
     * 用户唯一标识
     */
    @SerializedName("openid")
    private String openId;

    /**
     * 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回
     */
    @SerializedName("unionid")
    private String unionId;

    /**
     * 错误码
     */
    @SerializedName("errcode")
    private Integer errCode;

    /**
     * 错误信息
     */
    @SerializedName("errmsg")
    private String errMsg;

    public static JsCode2Session fromJson(String json) {
        return GsonBuilderUtil.create().fromJson(json, JsCode2Session.class);
    }
}

package com.dili.customer.domain.wechat;

import com.dili.customer.utils.GsonBuilderUtil;
import lombok.Data;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/10 17:19
 */
@Data
public class AppletPhone {
    /**
     * 用户绑定的手机号（国外手机号会有区号）
     */
    private String phoneNumber;
    /**
     * 没有区号的手机号
     */
    private String purePhoneNumber;
    /**
     * 区号
     */
    private String countryCode;

    /**
     *
     */
    private Watermark watermark;

    public static AppletPhone fromJson(String json) {
        return GsonBuilderUtil.create().fromJson(json, AppletPhone.class);
    }

}

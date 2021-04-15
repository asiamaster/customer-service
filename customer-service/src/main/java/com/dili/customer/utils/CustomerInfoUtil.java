package com.dili.customer.utils;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <B>客户信息相关的辅助处理类</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/4/26 18:13
 */
public class CustomerInfoUtil {

    /**
     * 证件号信息打码隐藏 如下是默认隐藏规则
     * 1.身份证隐藏规则：如果为15位证件号隐藏第7-12位用*表示，例如342123******123；如果为18位证件号隐藏第7-14位用*表示，例如：342112********1234；
     * 2.其他证件号隐藏规则：保留最后四位显示状态，前面都用*表示，例如：*********1234；
     * @param certificateNumber 证件号
     * @return
     */
    public static String certificateNumberHide(String certificateNumber) {
        if (StrUtil.isBlank(certificateNumber)) {
            return "";
        }
        //18位身份证
        if (IdcardUtil.isValidCard18(certificateNumber)) {
            return hide(certificateNumber, 6, 4);
        }
        //15位身份证
        if (IdcardUtil.isValidCard15(certificateNumber)) {
            return hide(certificateNumber, 6, 3);
        }
        return hide(certificateNumber, 0, 4);
    }

    /**
     * 隐藏指定位置的几个身份证号数字为“*”
     * @param data 需要隐藏的数据
     * @param startInclude 开始位置（包含）
     * @param endLength 结尾需要保留字符长度(包含)
     * @return
     */
    public static String hide(String data, int startInclude, int endLength) {
        if (StrUtil.isBlank(data)) {
            return "";
        }
        return IdcardUtil.hide(data, startInclude, data.length() - endLength);
    }
}

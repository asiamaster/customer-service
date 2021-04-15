package com.dili.customer.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * GsonBuilder工具类
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/10 17:24
 */
public class GsonBuilderUtil {
    private static final GsonBuilder INSTANCE = new GsonBuilder();

    public static Gson create() {
        return INSTANCE.create();
    }
}

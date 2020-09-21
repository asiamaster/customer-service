package com.dili.customer.sdk.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 将枚举类属性字段对应转换成json格式
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/7/16 15:12
 */
public class EnumUtil {

    /**
     * 枚举类型转换为json格式对象
     * @param enumClass
     * @return
     */
    public static Object toObject(Class<? extends Enum> enumClass) {
        try {
            Method methodValues = enumClass.getMethod("values");
            Object invoke = methodValues.invoke(null);
            int length = java.lang.reflect.Array.getLength(invoke);
            List<Object> values = new ArrayList<Object>();
            for (int i = 0; i < length; i++) {
                values.add(java.lang.reflect.Array.get(invoke, i));
            }
            SerializeConfig config = new SerializeConfig();
            config.configEnumAsJavaBean(enumClass);
            return JSON.parse(JSON.toJSONString(values, config));
        } catch (Exception e) {
            return null;
        }
    }
}

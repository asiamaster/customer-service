package com.dili.customer.commons.config;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.customer.sdk.enums.CustomerEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Objects;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2020/12/23 15:08
 */
@Configuration("customerCommonConfig")
public class CustomerCommonConfig {

    /**
     * 市场客户初始级别
     */
    @Value("#{${dili.customer.init.market.grade}}")
    private Map<Long, Integer> initGradeMap;


    /**
     * 获取某市场是否特定新增的客户级别
     * @param marketId 市场code
     * @return 如果存在，返回定制级别，否则默认会普通
     */
    public CustomerEnum.Grade getDefaultGrade(Long marketId) {
        if (CollectionUtil.isNotEmpty(initGradeMap) && Objects.nonNull(marketId) && initGradeMap.containsKey(marketId)) {
            return CustomerEnum.Grade.getInstance(initGradeMap.get(marketId));
        }
        return CustomerEnum.Grade.GENERAL;
    }
}

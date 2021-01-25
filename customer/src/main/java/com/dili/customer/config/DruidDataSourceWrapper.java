package com.dili.customer.config;

import com.alibaba.druid.filter.FilterManager;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

/**
 * druid 数据源wrapper
 * 重写方法主要解决 wall 中 multiStatementAllow 属性通过配置文件设置无效的问题
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/25 15:01
 */
@Configuration("druidDataSourceWrapper")
@ConfigurationProperties("spring.datasource.druid")
public class DruidDataSourceWrapper extends DruidDataSource implements InitializingBean {

    @Autowired
    private DataSourceProperties basicProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        //if not found prefix 'spring.datasource.druid' jdbc properties ,'spring.datasource' prefix jdbc properties will be used.
        if (super.getUsername() == null) {
            super.setUsername(basicProperties.determineUsername());
        }
        if (super.getPassword() == null) {
            super.setPassword(basicProperties.determinePassword());
        }
        if (super.getUrl() == null) {
            super.setUrl(basicProperties.determineUrl());
        }
        if (super.getDriverClassName() == null) {
            super.setDriverClassName(basicProperties.getDriverClassName());
        }
    }


    @Override
    public void addFilters(String filters) throws SQLException {
        if (filters == null || filters.length() == 0) {
            return;
        }
        String[] filterArray = filters.split("\\,");
        for (String item : filterArray) {
            if ("wall".equalsIgnoreCase(item.trim())) {
                WallConfig wallConfig = new WallConfig();
                wallConfig.setMultiStatementAllow(true);
                wallConfig.setNoneBaseStatementAllow(true);
                WallFilter wallFilter = new WallFilter();
                wallFilter.setConfig(wallConfig);
                this.filters.add(wallFilter);
                continue;
            }
            FilterManager.loadFilter(this.filters, item.trim());
        }
    }

    /**
     * Ignore the 'maxEvictableIdleTimeMillis < minEvictableIdleTimeMillis' validate,
     * it will be validated again in {@link DruidDataSource#init()}.
     *
     * for fix issue #3084, #2763
     *
     * @since 1.1.14
     */
    @Override
    public void setMaxEvictableIdleTimeMillis(long maxEvictableIdleTimeMillis) {
        try {
            super.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);
        } catch (IllegalArgumentException ignore) {
            super.maxEvictableIdleTimeMillis = maxEvictableIdleTimeMillis;
        }
    }
}

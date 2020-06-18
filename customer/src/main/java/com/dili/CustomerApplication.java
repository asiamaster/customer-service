package com.dili;

import com.dili.ss.dto.DTOScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 由MyBatis Generator工具自动生成
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.dili.customer.mapper", "com.dili.ss.dao"})
@ComponentScan(basePackages={"com.dili.ss","com.dili.customer"})
@DTOScan(value={"com.dili.ss", "com.dili.customer.domain"})
@EnableDiscoveryClient
public class CustomerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }

}

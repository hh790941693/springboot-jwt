package com.pjb.springbootjwt.common.mybatisplus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;

@Configuration
public class MyBatisConfig {

    /**
     * 分页插件
     *
     * @return
     * @author zhongweiyuan
     * @date 2018年4月14日下午4:13:15
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }

}

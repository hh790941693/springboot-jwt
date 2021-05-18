package com.pjb.springbootjwt.zhddkk.config;

import com.pjb.springbootjwt.zhddkk.interceptor.ActionLogInterceptor;
import com.pjb.springbootjwt.zhddkk.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器注册.
 */
@Configuration
public class InterceptorConfigNew implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 接口日志拦截器
        registry.addInterceptor(actionLogInterceptor())
                .addPathPatterns("/**");

        // 登录拦截器
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**");
    }

    @Bean
    public ActionLogInterceptor actionLogInterceptor() {
        return new ActionLogInterceptor();
    }

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }
}

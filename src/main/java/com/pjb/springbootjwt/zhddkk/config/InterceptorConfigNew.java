package com.pjb.springbootjwt.zhddkk.config;

import com.pjb.springbootjwt.zhddkk.interceptor.ActionLogInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfigNew implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(actionLogInterceptor())
                .addPathPatterns("/**");
    }

    @Bean
    public ActionLogInterceptor actionLogInterceptor() {
        return new ActionLogInterceptor();
    }
}

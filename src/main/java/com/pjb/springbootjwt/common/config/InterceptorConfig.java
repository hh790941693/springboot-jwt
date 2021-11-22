package com.pjb.springbootjwt.common.config;

import com.pjb.springbootjwt.common.interceptor.AuthenticationInterceptor;
import com.pjb.springbootjwt.common.interceptor.AvoidDuplicateSubmissionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author jinbin
 * @date 2018-07-08 22:33
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**");
        registry.addInterceptor(avoidSubmitInterceptor())
                .addPathPatterns("/**");
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Bean
    public AvoidDuplicateSubmissionInterceptor avoidSubmitInterceptor() {
        return new AvoidDuplicateSubmissionInterceptor();
    }
}

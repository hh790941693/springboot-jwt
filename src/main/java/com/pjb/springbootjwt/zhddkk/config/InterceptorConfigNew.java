package com.pjb.springbootjwt.zhddkk.config;

import com.pjb.springbootjwt.zhddkk.interceptor.ActionLogInterceptor;
import com.pjb.springbootjwt.zhddkk.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 拦截器注册.
 */
@Configuration
public class InterceptorConfigNew implements WebMvcConfigurer {
    // 登录拦截忽略的URL
    private static final List<String> IGNORE_URL_LIST = new ArrayList<>(Arrays.asList(
            "",
            "/",
            "/index",
            "/login.do",
            "/exception.page",
            "/redirect",
            "/valid/**",
            "/verifyUser.do",
            "/canvas/snow.page",
            "/querySystemInfo",
            "/register.page",
            "/forgetPassword.page",
            "/canvas/canvasIndex.page",
            "/getUserQuestion.json",
            "/showQRCode.do",
            "/checkUserRegisterStatus.json",
            "/updatePassword.do",
            "/generateVerifyCode.do",
            "/upload/app",
            "/register.do",
            "/getChatRoomInfo.json"));

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 接口日志拦截器
        registry.addInterceptor(actionLogInterceptor())
                .addPathPatterns("/**").excludePathPatterns("/js/**", "/img/**", "/css/**", "/i18n/**");

        // 登录拦截器 检查session是否存在
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**").excludePathPatterns(IGNORE_URL_LIST);
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

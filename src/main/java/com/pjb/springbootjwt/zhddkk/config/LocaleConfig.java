package com.pjb.springbootjwt.zhddkk.config;

import java.util.Locale;

import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * 国际化注册.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class LocaleConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        // 禁用session存储locale,是因为其会主动创建session(尚未登录的情况下不允许先创建session)
        //SessionLocaleResolver srl = new SessionLocaleResolver();

        // 使用Cookie存储locale
        CookieLocaleResolver slr = new CookieLocaleResolver();
        // 默认语言
        slr.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        // cookie名称
        slr.setCookieName(CommonConstants.C_LANG);
        // 失效时间
        slr.setCookieMaxAge(CommonConstants.LOCALE_COOKIE_EXPIRE);

        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        // 参数名
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

}
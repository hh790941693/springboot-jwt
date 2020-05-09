package com.jscxrz.zhddkk.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by songhairui on 2016/11/16.
 * @Configuration:
让spring加载该类配置
 * @EnableSwagger2:启用swagger
 * @EnableWebMvc:启用MVC
 * @ComponentScan:设置需要扫描的包，基本是swagger注解所在的包，例如Controller
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
@ComponentScan("com.jscxrz.zhddkk.swagger")
public class SwaggerConfig {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.jscxrz.zhddkk.swagger"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("api: localhost:8080/zhddkk/v2/api-docs <br/> swagger: http://localhost:8888/zhddkk/swagger-ui.html")
                .description("服务端 RESTFul Apis")
                .version("1.0")
                .build();
    }
}

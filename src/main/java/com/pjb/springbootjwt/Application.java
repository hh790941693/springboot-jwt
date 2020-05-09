package com.pjb.springbootjwt;

import com.pjb.springbootjwt.common.springbootListener.FailedListener;
import com.pjb.springbootjwt.common.springbootListener.StartedListener;
import com.pjb.springbootjwt.common.springbootListener.ClosedListener;
import com.pjb.springbootjwt.common.utils.SpringContextHolder;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@EnableTransactionManagement
@SpringBootApplication
@EnableCaching
@EnableAspectJAutoProxy
@MapperScan({"com.pjb.springbootjwt.**.dao"})
public class Application {

    private static Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication sa = new SpringApplication(Application.class);
        sa.addListeners(new StartedListener());
        sa.addListeners(new FailedListener());
        sa.addListeners(new ClosedListener());
        sa.run(args);
        //SpringApplication.run(Application.class, args);

        printProjectConfigs();
    }

    private static void printProjectConfigs() {
        ServerProperties serverProperties = SpringContextHolder.getApplicationContext().getBean(ServerProperties.class);
        DataSourceProperties dataSourceProperties = SpringContextHolder.getApplicationContext().getBean(DataSourceProperties.class);
        log.info("数据库：" + dataSourceProperties.getUrl());
        String contextPath = (serverProperties.getServlet().getContextPath() == null ? "" : serverProperties.getServlet().getContextPath());
        log.info("==================> run at http://localhost:" + serverProperties.getPort() + contextPath + "  <==================");
    }

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}

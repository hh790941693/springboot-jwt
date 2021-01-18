package com.pjb.springbootjwt;

import com.pjb.springbootjwt.common.springbootListener.ClosedListener;
import com.pjb.springbootjwt.common.springbootListener.FailedListener;
import com.pjb.springbootjwt.common.springbootListener.StartedListener;
import com.pjb.springbootjwt.common.utils.SpringContextHolder;
import java.io.File;
import java.net.InetAddress;
import java.util.Properties;
import javax.servlet.MultipartConfigElement;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

//自动扫描与当前类的同包以及子包
@ServletComponentScan
@EnableTransactionManagement
@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableAspectJAutoProxy
@MapperScan({"com.pjb.springbootjwt.**.dao"})
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static final String SPRING_TOMCAT_TEMP_LIUNX = "/opt/tomcatTemp";

    private static final String SPRING_TOMCAT_TEMP_WINDOW = "C:\\tomcatTemp";

    /**
     * main方法.
     * @param args 參數
     */
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
        InetAddress inetAddress = serverProperties.getAddress();
        String hostAddress = inetAddress.getHostAddress();
        String contextPath = (serverProperties.getServlet().getContextPath() == null ? "" : serverProperties.getServlet().getContextPath());
        log.info("==================> run at http://" + hostAddress + ":" + serverProperties.getPort() + contextPath + "  <==================");
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 解决文件上传,临时文件夹被程序自动删除问题.
     * 文件上传时自定义临时路径
     * @return MultipartConfigElement
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        Properties props = System.getProperties();
        String osName = props.getProperty("os.name");
        log.info("操作系统的名称:{}", osName);

        MultipartConfigFactory factory = new MultipartConfigFactory();
        String tomcatTmpPath = SPRING_TOMCAT_TEMP_LIUNX;
        //2.该处就是指定的路径(需要提前创建好目录，否则上传时会抛出异常)
        if (osName.contains("windows") || osName.contains("Windows")) {
            tomcatTmpPath = SPRING_TOMCAT_TEMP_WINDOW;
        }

        //创建springboot临时目录
        if (StringUtils.isNotEmpty(tomcatTmpPath)) {
            log.info("准备创建tomcat临时目录:{}", tomcatTmpPath);
            //System.out.println("准备创建tomcat临时目录:"+tomcatTmpPath);
            try {
                File file = new File(tomcatTmpPath);
                if (!file.exists()) {
                    if (file.mkdirs()) {
                        log.info("创建临时目录成功");
                        //System.out.println("创建临时目录成功");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.info("创建临时目录异常:{}", e.getMessage());
                //System.out.println("创建临时目录异常:"+e.getMessage());
            }
        }
        return factory.createMultipartConfig();
    }
}

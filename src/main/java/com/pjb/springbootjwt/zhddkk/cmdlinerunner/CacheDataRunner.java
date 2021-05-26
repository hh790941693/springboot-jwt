package com.pjb.springbootjwt.zhddkk.cmdlinerunner;


import com.pjb.springbootjwt.zhddkk.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * tomcat启动后启动完后自动执行缓存操作.
 */
@Component
public class CacheDataRunner implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(CacheDataRunner.class);

    @Autowired
    private CacheService cacheService;

    @Override
    public void run(String... args) throws Exception {
        // 缓存数据
        cacheService.cacheAllData();
    }
}

package com.pjb.springbootjwt.common.cmdLineRunner;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AutoStart implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(AutoStart.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info("---------------auto run------------------------");
    }
}

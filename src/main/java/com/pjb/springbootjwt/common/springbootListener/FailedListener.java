package com.pjb.springbootjwt.common.springbootListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;

public class FailedListener implements ApplicationListener<ApplicationFailedEvent> {

    private static Logger logger = LoggerFactory.getLogger(FailedListener.class);

    @Override
    public void onApplicationEvent(ApplicationFailedEvent applicationFailedEvent) {
        logger.info("springboot启动异常");
    }
}

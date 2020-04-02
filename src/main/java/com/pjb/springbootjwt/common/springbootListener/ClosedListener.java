package com.pjb.springbootjwt.common.springbootListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

public class ClosedListener implements ApplicationListener<ContextClosedEvent>  {

    private static Logger logger = LoggerFactory.getLogger(ClosedListener.class);

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        logger.info("springboot已停止");
    }
}

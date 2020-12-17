package com.pjb.springbootjwt.zhddkk.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask3 implements ScheduledOfTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask3.class);

    private int i;

    @Override
    public void execute() {
        logger.info("thread id:{},ScheduledTask3 execute times:{}", Thread.currentThread().getId(), ++i);
    }

}

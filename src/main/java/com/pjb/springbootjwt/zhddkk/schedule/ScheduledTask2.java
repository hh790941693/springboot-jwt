package com.pjb.springbootjwt.zhddkk.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask2 implements ScheduledOfTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask2.class);

    private int i;

    @Override
    public void execute() {
        logger.info("thread id:{},ScheduledTask2 execute times:{}", Thread.currentThread().getId(), ++i);
    }

}

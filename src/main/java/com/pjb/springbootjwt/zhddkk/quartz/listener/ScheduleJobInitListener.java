package com.pjb.springbootjwt.zhddkk.quartz.listener;

import com.pjb.springbootjwt.zhddkk.quartz.QuartzManager;
import com.pjb.springbootjwt.zhddkk.service.SysTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * </pre>
 * <small> 2018年3月23日 | Aron</small>
 */
@Component
@Order(value = 1)
public class ScheduleJobInitListener implements CommandLineRunner {

    @Autowired
    SysTaskService scheduleJobService;

    @Autowired
    QuartzManager quartzManager;

    @Override
    public void run(String... arg0) throws Exception {
        try {
            scheduleJobService.initSchedule();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
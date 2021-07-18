package com.pjb.springbootjwt.zhddkk.config;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

@Configuration
public class ScheduledConfig implements SchedulingConfigurer {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private WsScheduledCronService wsScheduledCronService;
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        List<WsScheduledCronDO> list = wsScheduledCronService.selectList(new EntityWrapper<WsScheduledCronDO>().ne("status", "0"));
        for (WsScheduledCronDO wsScheduledCronDO : list) {
            Class<?> clazz;
            Object task;
            try {
                clazz = Class.forName(wsScheduledCronDO.getCronKey());
                task = context.getBean(clazz);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("spring_scheduled_cron表数据" + wsScheduledCronDO.getCronKey() + "有误", e);
            } catch (BeansException e) {
                throw new IllegalArgumentException(wsScheduledCronDO.getCronKey() + "未纳入到spring管理", e);
            }
            // 可以通过改变数据库数据进而实现动态改变执行周期
            taskRegistrar.addTriggerTask(((Runnable) task),
                    triggerContext -> {
                        String cronExpression = wsScheduledCronDO.getCronExpression();
                        return new CronTrigger(cronExpression).nextExecutionTime(triggerContext);
                    }
            );
        }
    }
    @Bean
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(10);
    }
}

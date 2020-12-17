package com.pjb.springbootjwt.zhddkk.schedule;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.common.utils.SpringContextHolder;
import com.pjb.springbootjwt.zhddkk.domain.WsScheduledCronDO;
import com.pjb.springbootjwt.zhddkk.service.WsScheduledCronService;

public interface ScheduledOfTask extends Runnable {
    /**
     * 定时任务方法.
     */
    void execute();
    
    /**
     * 实现控制定时任务启用或禁用的功能.
     */
    @Override
    default void run() {
        WsScheduledCronService wsScheduledCronService = SpringContextHolder.getBean(WsScheduledCronService.class);
        WsScheduledCronDO wsScheduledCronDO = wsScheduledCronService
            .selectOne(new EntityWrapper<WsScheduledCronDO>().eq("cron_key", this.getClass().getName()));
        if ("2".equals(wsScheduledCronDO.getStatus())) {
            // 任务是禁用状态
            return;
        }
        execute();
    }
}

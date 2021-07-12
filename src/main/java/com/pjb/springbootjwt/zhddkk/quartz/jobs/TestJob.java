package com.pjb.springbootjwt.zhddkk.quartz.jobs;

import com.sun.xml.internal.ws.spi.db.DatabindingException;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * <pre>
 * </pre>
 * 
 * <small> 2018年3月22日 | Aron</small>
 */
@Component
public class TestJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        System.out.println(new Date()+"运行quartz任务:Test");
        System.out.println("任务详情如下:");
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }
}

package com.pjb.springbootjwt.zhddkk.quartz;

import lombok.Data;
import java.io.Serializable;

/**
 * <pre>
 * </pre>
 * <small> 2018年3月23日 | Aron</small>
 */
@SuppressWarnings("serial")
@Data
public class ScheduleJob implements Serializable {

    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务分组
     */
    private String jobGroup;
    /**
     * 任务执行时调用哪个类的方法 包名+类名
     */
    private String beanClass;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 方法名
     */
    private String methodName;

    // 方法参数
    private String parameters;

    /**
     * 任务状态 是否启动任务
     */
    private String jobStatus;

    /**
     * 描述
     */
    private String description;

    /**
     * 任务是否有状态
     */
    private String isConcurrent;
}
package com.pjb.springbootjwt.zhddkk.util;

import com.pjb.springbootjwt.zhddkk.domain.SysTaskDO;
import com.pjb.springbootjwt.zhddkk.quartz.ScheduleJob;

public class ScheduleJobUtils {
	public static ScheduleJob entityToData(SysTaskDO scheduleJobEntity) {
		ScheduleJob scheduleJob = new ScheduleJob();
		scheduleJob.setJobName(scheduleJobEntity.getJobName());
		scheduleJob.setJobGroup(scheduleJobEntity.getJobGroup());
		scheduleJob.setBeanClass(scheduleJobEntity.getBeanClass());
		scheduleJob.setCronExpression(scheduleJobEntity.getCronExpression());
		scheduleJob.setMethodName(scheduleJobEntity.getMethodName());
		scheduleJob.setParameters(scheduleJobEntity.getParameters());
		scheduleJob.setDescription(scheduleJobEntity.getDescription());
		scheduleJob.setJobStatus(scheduleJobEntity.getJobStatus());
		scheduleJob.setIsConcurrent(scheduleJobEntity.getIsConcurrent());
		scheduleJob.setSpringBean(scheduleJobEntity.getSpringBean());
		return scheduleJob;
	}
}
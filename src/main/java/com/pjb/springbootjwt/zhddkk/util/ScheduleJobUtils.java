package com.pjb.springbootjwt.zhddkk.util;

import com.pjb.springbootjwt.zhddkk.domain.SysTaskDO;
import com.pjb.springbootjwt.zhddkk.quartz.ScheduleJob;

public class ScheduleJobUtils {
	public static ScheduleJob entityToData(SysTaskDO scheduleJobEntity) {
		ScheduleJob scheduleJob = new ScheduleJob();
		scheduleJob.setBeanClass(scheduleJobEntity.getBeanClass());
		scheduleJob.setCronExpression(scheduleJobEntity.getCronExpression());
		scheduleJob.setDescription(scheduleJobEntity.getDescription());
		scheduleJob.setIsConcurrent(scheduleJobEntity.getIsConcurrent());
		scheduleJob.setJobName(scheduleJobEntity.getJobName());
		scheduleJob.setJobGroup(scheduleJobEntity.getJobGroup());
		scheduleJob.setJobStatus(scheduleJobEntity.getJobStatus());
		scheduleJob.setMethodName(scheduleJobEntity.getMethodName());
		scheduleJob.setSpringBean(scheduleJobEntity.getSpringBean());
		return scheduleJob;
	}
}
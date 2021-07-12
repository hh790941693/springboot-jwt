package com.pjb.springbootjwt.zhddkk.service;

import com.pjb.springbootjwt.zhddkk.base.CoreService;
import com.pjb.springbootjwt.zhddkk.domain.SysTaskDO;
import org.quartz.SchedulerException;


/**
 * <pre>
 * 定时任务
 * </pre>
 * <small> 2018年3月22日 | Aron</small>
 */
public interface SysTaskService extends CoreService<SysTaskDO> {
	
	void initSchedule() throws SchedulerException;

	void changeStatus(Long jobId, String cmd) throws SchedulerException;

	void updateCron(Long jobId) throws SchedulerException;
}

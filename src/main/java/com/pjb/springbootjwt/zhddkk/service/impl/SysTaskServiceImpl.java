package com.pjb.springbootjwt.zhddkk.service.impl;

import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import com.pjb.springbootjwt.zhddkk.dao.SysTaskDao;
import com.pjb.springbootjwt.zhddkk.domain.SysTaskDO;
import com.pjb.springbootjwt.zhddkk.quartz.QuartzManager;
import com.pjb.springbootjwt.zhddkk.quartz.ScheduleJob;
import com.pjb.springbootjwt.zhddkk.service.SysTaskService;
import com.pjb.springbootjwt.zhddkk.util.ScheduleJobUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * </pre>
 * <small> 2018年3月23日 | Aron</small>
 */
@Service
public class SysTaskServiceImpl extends CoreServiceImpl<SysTaskDao, SysTaskDO> implements SysTaskService {

    // 停止计划任务
    public static String STATUS_RUNNING_STOP = "stop";
    // 开启计划任务
    public static String STATUS_RUNNING_START = "start";

    @Autowired
    QuartzManager quartzManager;

    @Override
    public boolean deleteById(Serializable id) {
        try {
            SysTaskDO scheduleJob = selectById(id);
            quartzManager.deleteJob(ScheduleJobUtils.entityToData(scheduleJob));
            return retBool(baseMapper.deleteById(id));
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean deleteBatchIds(List<? extends Serializable> ids) {
        for (Serializable id : ids) {
            try {
                SysTaskDO scheduleJob = selectById(id);
                quartzManager.deleteJob(ScheduleJobUtils.entityToData(scheduleJob));
            } catch (SchedulerException e) {
                e.printStackTrace();
                return false;
            }
        }
        return retBool(baseMapper.deleteBatchIds(ids));
    }

    @Override
    public void initSchedule() throws SchedulerException {
        // 这里获取任务信息数据
        List<SysTaskDO> jobList = baseMapper.selectList(null);
        for (SysTaskDO scheduleJob : jobList) {
            if ("1".equals(scheduleJob.getJobStatus())) {
                ScheduleJob job = ScheduleJobUtils.entityToData(scheduleJob);
                quartzManager.addJob(job);
            }

        }
    }

    @Override
    public void changeStatus(Long jobId, String cmd) throws SchedulerException {
        SysTaskDO scheduleJob = selectById(jobId);
        if (scheduleJob == null) {
            return;
        }
        if (STATUS_RUNNING_STOP.equals(cmd)) {
            quartzManager.deleteJob(ScheduleJobUtils.entityToData(scheduleJob));
            scheduleJob.setJobStatus(ScheduleJob.STATUS_NOT_RUNNING);
        } else {
            if (!STATUS_RUNNING_START.equals(cmd)) {
            } else {
                scheduleJob.setJobStatus(ScheduleJob.STATUS_RUNNING);
                quartzManager.addJob(ScheduleJobUtils.entityToData(scheduleJob));
            }
        }
        updateById(scheduleJob);
    }

    @Override
    public void updateCron(Long jobId) throws SchedulerException {
        SysTaskDO scheduleJob = selectById(jobId);
        if (scheduleJob == null) {
            return;
        }
        if (ScheduleJob.STATUS_RUNNING.equals(scheduleJob.getJobStatus())) {
            quartzManager.updateJobCron(ScheduleJobUtils.entityToData(scheduleJob));
        }
        updateById(scheduleJob);
    }

}

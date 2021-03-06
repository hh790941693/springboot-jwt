package com.pjb.springbootjwt.zhddkk.controller;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.domain.SysTaskDO;
import com.pjb.springbootjwt.zhddkk.quartz.JobConstant;
import com.pjb.springbootjwt.zhddkk.service.SysTaskService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;

/**
 * <pre>
 * 定时任务
 * </pre>
 * 
 * <small> 2018年3月23日 | Aron</small>
 */
@Controller
@RequestMapping("/zhddkk/sysTask")
public class SysTaskController extends AdminBaseController {
    @Autowired
    private SysTaskService sysTaskService;

    @GetMapping()
    String taskScheduleJob() {
        return "zhddkk/sysTask/sysTask";
    }

    @ResponseBody
    @GetMapping("/list")
    public Result<Page<SysTaskDO>> list(SysTaskDO taskDTO) {
        // 查询列表数据
        Wrapper<SysTaskDO> wrapper = new EntityWrapper<SysTaskDO>();
        if (StringUtils.isNotBlank(taskDTO.getJobName())) {
            wrapper.like("job_name", taskDTO.getJobName(), SqlLike.DEFAULT);
        }
        Page<SysTaskDO> page = sysTaskService.selectPage(getPage(SysTaskDO.class), wrapper);
        return Result.ok(page);
    }

    @GetMapping("/add")
    String add(Model model) {
        SysTaskDO sysTaskDO = new SysTaskDO();
        sysTaskDO.setMethodName(JobConstant.DEFAULT_JOB_METHOD_NAME);
        model.addAttribute("sysTaskDO", sysTaskDO);
        return "zhddkk/sysTask/add";
    }

    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Long id, Model model) {
        SysTaskDO sysTaskDO = sysTaskService.selectById(id);
        model.addAttribute("sysTaskDO", sysTaskDO);
        return "zhddkk/sysTask/edit";
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public Result<SysTaskDO> info(@PathVariable("id") Long id) {
        SysTaskDO taskScheduleJob = sysTaskService.selectById(id);
        return Result.ok(taskScheduleJob);
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    public Result<String> save(SysTaskDO taskScheduleJob) {
        if (StringUtils.isBlank(taskScheduleJob.getMethodName())) {
            taskScheduleJob.setMethodName(JobConstant.DEFAULT_JOB_METHOD_NAME);
        }
        taskScheduleJob.setCreateDate(new Date());
        taskScheduleJob.setUpdateDate(new Date());
        sysTaskService.insert(taskScheduleJob);
        return Result.ok();
    }

    /**
     * 修改
     */
    @ResponseBody
    @PostMapping("/update")
    public Result<String> update(SysTaskDO taskScheduleJob) {
        if (StringUtils.isBlank(taskScheduleJob.getMethodName())) {
            taskScheduleJob.setMethodName(JobConstant.DEFAULT_JOB_METHOD_NAME);
        }
        taskScheduleJob.setUpdateDate(new Date());
        sysTaskService.updateById(taskScheduleJob);
        return Result.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    public Result<String> remove(Long id) {
        sysTaskService.deleteById(id);
        return Result.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    public Result<String> remove(@RequestParam("ids[]") Long[] ids) {
        sysTaskService.deleteBatchIds(Arrays.asList(ids));
        return Result.ok();
    }

    @PostMapping(value = "/changeJobStatus")
    @ResponseBody
    public Result<String> changeJobStatus(Long id, String cmd) {
        String label = "停止";
        if ("start".equals(cmd)) {
            label = "启动";
        } else {
            label = "停止";
        }
        try {
            sysTaskService.changeStatus(id, cmd);
            return Result.ok("任务" + label + "成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok("任务" + label + "失败");
    }
}

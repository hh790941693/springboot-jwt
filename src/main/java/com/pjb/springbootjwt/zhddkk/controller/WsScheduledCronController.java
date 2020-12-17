package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.pjb.springbootjwt.zhddkk.base.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.zhddkk.domain.WsScheduledCronDO;
import com.pjb.springbootjwt.zhddkk.service.WsScheduledCronService;
import com.pjb.springbootjwt.zhddkk.schedule.ScheduledOfTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时任务表.
 */
@Controller
@RequestMapping("/zhddkk/wsScheduledCron")
public class WsScheduledCronController extends AdminBaseController {
    
    private static final Logger logger = LoggerFactory.getLogger(WsScheduledCronController.class);

    @Autowired
    private ApplicationContext context;

    /**
     * binder.
     * 
     * @param binder binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
    
    @Autowired
    private WsScheduledCronService wsScheduledCronService;
    
    /**
     * 跳转到定时任务表页面.
     */
    @GetMapping()
    // @RequiresPermissions("zhddkk:wsScheduledCron:wsScheduledCron")
    public String wsScheduledCron() {
        return "zhddkk/wsScheduledCron/wsScheduledCron";
    }
    
    /**
     * 获取定时任务表列表数据.
     */
    @ResponseBody
    @GetMapping("/list")
    // @RequiresPermissions("zhddkk:wsScheduledCron:wsScheduledCron")
    public Result<Page<WsScheduledCronDO>> list(WsScheduledCronDO wsScheduledCronDto) {
        Wrapper<WsScheduledCronDO> wrapper = new EntityWrapper<WsScheduledCronDO>(wsScheduledCronDto);
        Page<WsScheduledCronDO> page = wsScheduledCronService.selectPage(getPage(WsScheduledCronDO.class), wrapper);
        return Result.ok(page);
    }
    
    /**
     * 跳转到定时任务表添加页面.
     */
    @GetMapping("/add")
    // @RequiresPermissions("zhddkk:wsScheduledCron:add")
    public String add(Model model) {
        WsScheduledCronDO wsScheduledCron = new WsScheduledCronDO();
        model.addAttribute("wsScheduledCron", wsScheduledCron);
        return "zhddkk/wsScheduledCron/wsScheduledCronForm";
    }
    
    /**
     * 跳转到定时任务表编辑页面.
     * 
     * @param cronId 定时任务表ID
     * @param model 定时任务表实体
     */
    @GetMapping("/edit/{cronId}")
    // @RequiresPermissions("zhddkk:wsScheduledCron:edit")
    public String edit(@PathVariable("cronId") Integer cronId, Model model) {
        WsScheduledCronDO wsScheduledCron = wsScheduledCronService.selectById(cronId);
        model.addAttribute("wsScheduledCron", wsScheduledCron);
        return "zhddkk/wsScheduledCron/wsScheduledCronForm";
    }
    
    /**
     * 保存定时任务表.
     */
    @ResponseBody
    @PostMapping("/save")
    // @RequiresPermissions("zhddkk:wsScheduledCron:add")
    public Result<String> save(WsScheduledCronDO wsScheduledCron) {
        wsScheduledCronService.insert(wsScheduledCron);
        return Result.ok();
    }
    
    /**
     * 编辑定时任务表.
     */
    @ResponseBody
    @RequestMapping("/update")
    // @RequiresPermissions("zhddkk:wsScheduledCron:edit")
    public Result<String> update(WsScheduledCronDO wsScheduledCron) {
        wsScheduledCronService.updateById(wsScheduledCron);
        return Result.ok();
    }
    
    /**
     * 删除定时任务表.
     */
    @PostMapping("/remove")
    @ResponseBody
    // @RequiresPermissions("zhddkk:wsScheduledCron:remove")
    public Result<String> remove(Integer cronId) {
        wsScheduledCronService.deleteById(cronId);
        return Result.ok();
    }
    
    /**
     * 批量删除定时任务表.
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    // @RequiresPermissions("zhddkk:wsScheduledCron:batchRemove")
    public Result<String> remove(@RequestParam("ids[]") Integer[] cronIds) {
        wsScheduledCronService.deleteBatchIds(Arrays.asList(cronIds));
        return Result.ok();
    }

    /**
     * 执行定时任务
     */
    @ResponseBody
    @RequestMapping("/runTaskCron")
    public Result<String> runTaskCron(String cronKey) throws Exception {
        ((ScheduledOfTask) context.getBean(Class.forName(cronKey))).execute();
        return Result.ok();
    }
    /**
     * 启用或禁用定时任务
     */
    @ResponseBody
    @RequestMapping("/changeStatusTaskCron")
    public Result<String> changeStatusTaskCron(Integer status, String cronKey) {
        WsScheduledCronDO wsScheduledCronDO = wsScheduledCronService.selectOne(new EntityWrapper<WsScheduledCronDO>().eq("cron_key", cronKey));
        if (null != wsScheduledCronDO){
            wsScheduledCronDO.setStatus(status);
            return Result.ok();
        }

        return Result.fail();
    }
}
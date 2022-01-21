package com.pjb.springbootjwt.zhddkk.controller;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.constants.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.constants.OperationEnum;
import com.pjb.springbootjwt.zhddkk.domain.WsFeedbackDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsFeedbackService;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * 问题反馈.
 */
@Controller
@RequestMapping("/zhddkk/wsFeedback")
public class WsFeedbackController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(WsFeedbackController.class);

    /**
     * binder.
     * @param binder binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Autowired
    private WsFeedbackService wsFeedbackService;

    @Autowired
    private WsUsersService wsUsersService;

    /**
    * 跳转到问题反馈页面.
    */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.FEED_BACK, subModule = "", describe = "我的反馈建议页面")
    @GetMapping("/myFeedback")
    //@RequiresPermissions("zhddkk:wsFeedback:wsFeedback")
    public String wsFeedback(Model model) {
        return "zhddkk/wsFeedback/wsFeedback";
    }

    /**
     * 跳转到问题反馈页面.
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.FEED_BACK, subModule = "", describe = "反馈建议页面(管理员)")
    @GetMapping("/adminFeedback")
    //@RequiresPermissions("zhddkk:wsFeedback:wsFeedback")
    public String adminFeedback() {
        return "zhddkk/wsFeedback/wsFeedbackForAdmin";
    }

    /**
     * 获取问题反馈列表数据.
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.FEED_BACK, subModule = "", describe = "我的反馈建议列表")
    @ResponseBody
    @GetMapping("/myFeedbackList")
    //@RequiresPermissions("zhddkk:wsFeedback:wsFeedback")
    public Result<Page<WsFeedbackDO>> myFeedbackList(WsFeedbackDO wsFeedbackDto) {
        Wrapper<WsFeedbackDO> wrapper = new EntityWrapper<WsFeedbackDO>();
        String sessionUser = SessionUtil.getSessionUserName();
        if (!sessionUser.equals(CommonConstants.ADMIN_USER)) {
            wrapper.eq("user_name", sessionUser);
        }
        if (null != wsFeedbackDto.getStatus()) {
            wrapper.eq("status", wsFeedbackDto.getStatus());
        }
        wrapper.ne("status", "0").orderBy("create_time", false);
        Page<WsFeedbackDO> page = wsFeedbackService.selectPage(getPage(WsFeedbackDO.class), wrapper);
        return Result.ok(page);
    }

    /**
     * 获取问题反馈列表数据.
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY, module = ModuleEnum.FEED_BACK, subModule = "", describe = "反馈建议列表(管理员)")
    @ResponseBody
    @GetMapping("/adminFeedbackList")
    //@RequiresPermissions("zhddkk:wsFeedback:wsFeedback")
    public Result<Page<WsFeedbackDO>> adminFeedbackList(WsFeedbackDO wsFeedbackDto) {
        Wrapper<WsFeedbackDO> wrapper = new EntityWrapper<WsFeedbackDO>();
        if (null != wsFeedbackDto.getStatus()) {
            wrapper.eq("status", wsFeedbackDto.getStatus());
        }
        wrapper.ne("status", "0").orderBy("status").orderBy("create_time", false);
        Page<WsFeedbackDO> page = wsFeedbackService.selectPage(getPage(WsFeedbackDO.class), wrapper);
        return Result.ok(page);
    }

    /**
     * 跳转到问题反馈添加页面.
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.FEED_BACK, subModule = "", describe = "添加反馈建议页面")
    @GetMapping("/add")
    //@RequiresPermissions("zhddkk:wsFeedback:add")
    public String add(Model model) {
        logger.info("进入添加问题反馈内容控制层");
        WsFeedbackDO wsFeedback = new WsFeedbackDO();
        wsFeedback.setUserName(SessionUtil.getSessionUserName());
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", SessionUtil.getSessionUserName()));
        wsFeedback.setUserId(wsUsersDO.getId());
        wsFeedback.setStatus(1);
        wsFeedback.setCreateTime(new Date());
        model.addAttribute("wsFeedback", wsFeedback);
        return "zhddkk/wsFeedback/wsFeedbackForm";
    }

    /**
     * 跳转到问题反馈编辑页面.
     * @param id 问题反馈ID
     * @param model 问题反馈实体
     */
    @GetMapping("/edit/{id}")
    //@RequiresPermissions("zhddkk:wsFeedback:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        WsFeedbackDO wsFeedback = wsFeedbackService.selectById(id);
        model.addAttribute("wsFeedback", wsFeedback);
        return "zhddkk/wsFeedback/wsFeedbackForm";
    }

    /**
     * 保存问题反馈.
     */
    @OperationLogAnnotation(type = OperationEnum.INSERT, module = ModuleEnum.FEED_BACK, subModule = "", describe = "保存反馈建议")
    @ResponseBody
    @PostMapping("/save")
    //@RequiresPermissions("zhddkk:wsFeedback:add")
    public Result<String> save(WsFeedbackDO wsFeedback) {
        wsFeedbackService.insert(wsFeedback);
        return Result.ok();
    }

    /**
     * 编辑问题反馈.
     */
    @ResponseBody
    @RequestMapping("/update")
    //@RequiresPermissions("zhddkk:wsFeedback:edit")
    public Result<String> update(WsFeedbackDO wsFeedback) {
        wsFeedbackService.updateById(wsFeedback);
        return Result.ok();
    }

    /**
     * 删除问题反馈.
     */
    @OperationLogAnnotation(type = OperationEnum.DELETE, module = ModuleEnum.FEED_BACK, subModule = "", describe = "删除反馈建议")
    @PostMapping("/remove")
    @ResponseBody
    //@RequiresPermissions("zhddkk:wsFeedback:remove")
    public Result<String> remove(Integer id) {
        WsFeedbackDO wsFeedbackDO = wsFeedbackService.selectById(id);
        if (null != wsFeedbackDO) {
            wsFeedbackDO.setStatus(0);
            wsFeedbackService.updateById(wsFeedbackDO);
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     * 批量删除问题反馈.
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    //@RequiresPermissions("zhddkk:wsFeedback:batchRemove")
    public Result<String> remove(@RequestParam("ids[]") Integer[] ids) {
        wsFeedbackService.deleteBatchIds(Arrays.asList(ids));
        return Result.ok();
    }

    /**
     * 回复.
     * @param id 建议id
     * @param replyContext 回复内容
     * @return 回复结果
     */
    @OperationLogAnnotation(type = OperationEnum.UPDATE, module = ModuleEnum.FEED_BACK, subModule = "", describe = "回复反馈建议(管理员)")
    @ResponseBody
    @PostMapping("/replyFeedback")
    public Result<String> replyFeedback(Integer id, String replyContext) {
        WsFeedbackDO wsFeedbackDO = wsFeedbackService.selectById(id);
        if (null == wsFeedbackDO) {
            return Result.fail("记录不存在");
        }

        if (wsFeedbackDO.getStatus().intValue() != 1) {
            return Result.fail("记录不是待答复状态");
        }

        wsFeedbackDO.setStatus(2);
        wsFeedbackDO.setReplyTime(new Date());
        wsFeedbackDO.setReplyContent(replyContext);

        boolean updateFlag = wsFeedbackService.updateById(wsFeedbackDO);
        if (updateFlag) {
            return Result.ok();
        }

        return Result.fail();
    }
}

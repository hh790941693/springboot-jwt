package com.pjb.springbootjwt.zhddkk.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.common.vo.Result;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.constants.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.constants.OperationEnum;
import com.pjb.springbootjwt.zhddkk.domain.WsChatlogDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsChatlogService;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
 * 聊天记录表.
 */
@Controller
@RequestMapping("/zhddkk/wsChatlog")
public class WsChatlogController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(WsChatlogController.class);

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
    private WsChatlogService wsChatlogService;

    @Autowired
    private WsUsersService wsUsersService;

    /**
    * 跳转到聊天记录表页面.
    */
    @OperationLogAnnotation(type = OperationEnum.PAGE,
            module = ModuleEnum.CHAT_HISTORY_MANAGE,
            subModule = "", describe = "聊天记录页面")
    @GetMapping()
    //@RequiresPermissions("zhddkk:wsChatlog:wsChatlog")
    public String wsChatlog(Model model) {
        List<WsUsersDO> allUserList = wsUsersService.selectList(new EntityWrapper<WsUsersDO>()
                .ne("name", CommonConstants.ADMIN_USER)
                .orderBy("name"));
        model.addAttribute("allUserList", allUserList);
        model.addAttribute("chatLogDO", new WsChatlogDO());
        return "zhddkk/wsChatlog/wsChatlog";
    }

    /**
     * 获取聊天记录表列表数据.
     */
    @OperationLogAnnotation(type = OperationEnum.QUERY,
            module = ModuleEnum.CHAT_HISTORY_MANAGE, subModule = "", describe = "聊天记录列表")
    @ResponseBody
    @GetMapping("/list")
    //@RequiresPermissions("zhddkk:wsChatlog:wsChatlog")
    public Result<Page<WsChatlogDO>> list(WsChatlogDO wsChatlogDto) {
        Wrapper<WsChatlogDO> wrapper = new EntityWrapper<WsChatlogDO>();
        if (StringUtils.isNotBlank(wsChatlogDto.getUser())) {
            wrapper.eq("user", wsChatlogDto.getUser());
        }
        if (StringUtils.isNotBlank(wsChatlogDto.getToUser())) {
            wrapper.eq("to_user", wsChatlogDto.getToUser());
        }
        if (StringUtils.isNotBlank(wsChatlogDto.getBeginTime())) {
            wrapper.ge("time", wsChatlogDto.getBeginTime());
        }
        if (StringUtils.isNotBlank(wsChatlogDto.getEndTime())) {
            wrapper.le("time", wsChatlogDto.getEndTime());
        }
        wrapper.isNotNull("to_user").ne("to_user", "").orderBy("time", false).orderBy("user");
        Page<WsChatlogDO> page = wsChatlogService.selectPage(getPage(WsChatlogDO.class), wrapper);
        return Result.ok(page);
    }

    /**
     * 跳转到聊天记录表添加页面.
     */
    @GetMapping("/add")
    //@RequiresPermissions("zhddkk:wsChatlog:add")
    public String add(Model model) {
        WsChatlogDO wsChatlog = new WsChatlogDO();
        model.addAttribute("wsChatlog", wsChatlog);
        return "zhddkk/wsChatlog/wsChatlogform";
    }

    /**
     * 跳转到聊天记录表编辑页面.
     * @param id 聊天记录表ID
     * @param model 聊天记录表实体
     */
    @GetMapping("/edit/{id}")
    //@RequiresPermissions("zhddkk:wsChatlog:edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        WsChatlogDO wsChatlog = wsChatlogService.selectById(id);
        model.addAttribute("wsChatlog", wsChatlog);
        return "zhddkk/wsChatlog/wsChatlogform";
    }

    /**
     * 保存聊天记录表.
     */
    @ResponseBody
    @PostMapping("/save")
    //@RequiresPermissions("zhddkk:wsChatlog:add")
    public Result<String> save(WsChatlogDO wsChatlog) {
        wsChatlogService.insert(wsChatlog);
        return Result.ok();
    }

    /**
     * 编辑聊天记录表.
     */
    @ResponseBody
    @RequestMapping("/update")
    //@RequiresPermissions("zhddkk:wsChatlog:edit")
    public Result<String> update(WsChatlogDO wsChatlog) {
        wsChatlogService.updateById(wsChatlog);
        return Result.ok();
    }

    /**
     * 删除聊天记录表.
     */
    @PostMapping("/remove")
    @ResponseBody
    //@RequiresPermissions("zhddkk:wsChatlog:remove")
    public Result<String> remove(Long id) {
        wsChatlogService.deleteById(id);
        return Result.ok();
    }

    /**
     * 批量删除聊天记录表.
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    //@RequiresPermissions("zhddkk:wsChatlog:batchRemove")
    public Result<String> remove(@RequestParam("ids[]") Long[] ids) {
        wsChatlogService.deleteBatchIds(Arrays.asList(ids));
        return Result.ok();
    }
}

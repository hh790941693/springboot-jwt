package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.zhddkk.domain.WsFeedbackDO;
import com.pjb.springbootjwt.zhddkk.service.WsFeedbackService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 问题反馈
 */
@Controller
@RequestMapping("/zhddkk/wsFeedback")
public class WsFeedbackController extends AdminBaseController {

    private static Logger logger = LoggerFactory.getLogger(WsFeedbackController.class);

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
    * 跳转到问题反馈页面
	*/
	@GetMapping("/myFeedback")
	//@RequiresPermissions("zhddkk:wsFeedback:wsFeedback")
	String wsFeedback(Model model, String user){
		model.addAttribute("username", user);
	    return "zhddkk/wsFeedback/wsFeedback";
	}

    /**
     * 跳转到问题反馈页面
     */
    @GetMapping("/adminFeedback")
    //@RequiresPermissions("zhddkk:wsFeedback:wsFeedback")
    String adminFeedback(){
        return "zhddkk/wsFeedback/wsFeedbackForAdmin";
    }

    /**
     * 获取问题反馈列表数据
     */
	@ResponseBody
	@GetMapping("/myFeedbackList")
	//@RequiresPermissions("zhddkk:wsFeedback:wsFeedback")
	public Result<Page<WsFeedbackDO>> myFeedbackList(WsFeedbackDO wsFeedbackDTO){
        Wrapper<WsFeedbackDO> wrapper = new EntityWrapper<WsFeedbackDO>();
        if (StringUtils.isNotBlank(wsFeedbackDTO.getUserName())){
            wrapper.eq("user_name", wsFeedbackDTO.getUserName());
        }
        wrapper.ne("status", "0").orderBy("create_time",false);
        Page<WsFeedbackDO> page = wsFeedbackService.selectPage(getPage(WsFeedbackDO.class), wrapper);
        return Result.ok(page);
	}

    /**
     * 获取问题反馈列表数据
     */
    @ResponseBody
    @GetMapping("/adminFeedbackList")
    //@RequiresPermissions("zhddkk:wsFeedback:wsFeedback")
    public Result<Page<WsFeedbackDO>> adminFeedbackList(WsFeedbackDO wsFeedbackDTO){
        Wrapper<WsFeedbackDO> wrapper = new EntityWrapper<WsFeedbackDO>();
        wrapper.ne("status", "0").orderBy("status").orderBy("create_time",false);
        Page<WsFeedbackDO> page = wsFeedbackService.selectPage(getPage(WsFeedbackDO.class), wrapper);
        return Result.ok(page);
    }

    /**
     * 跳转到问题反馈添加页面
     */
	@GetMapping("/add")
	//@RequiresPermissions("zhddkk:wsFeedback:add")
	String add(Model model, String user){
		WsFeedbackDO wsFeedback = new WsFeedbackDO();
        wsFeedback.setUserName(user);
        WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", user));
        wsFeedback.setUserId(wsUsersDO.getId());
        wsFeedback.setStatus(1);
        wsFeedback.setCreateTime(new Date());
        model.addAttribute("wsFeedback", wsFeedback);
	    return "zhddkk/wsFeedback/wsFeedbackForm";
	}

    /**
     * 跳转到问题反馈编辑页面
     * @param id 问题反馈ID
     * @param model 问题反馈实体
     */
	@GetMapping("/edit/{id}")
	//@RequiresPermissions("zhddkk:wsFeedback:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		WsFeedbackDO wsFeedback = wsFeedbackService.selectById(id);
		model.addAttribute("wsFeedback", wsFeedback);
	    return "zhddkk/wsFeedback/wsFeedbackForm";
	}
	
	/**
	 * 保存问题反馈
	 */
	@ResponseBody
	@PostMapping("/save")
	//@RequiresPermissions("zhddkk:wsFeedback:add")
	public Result<String> save( WsFeedbackDO wsFeedback){
		wsFeedbackService.insert(wsFeedback);
        return Result.ok();
	}

	/**
	 * 编辑问题反馈
	 */
	@ResponseBody
	@RequestMapping("/update")
	//@RequiresPermissions("zhddkk:wsFeedback:edit")
	public Result<String> update( WsFeedbackDO wsFeedback){
		wsFeedbackService.updateById(wsFeedback);
		return Result.ok();
	}
	
	/**
	 * 删除问题反馈
	 */
	@PostMapping("/remove")
	@ResponseBody
	//@RequiresPermissions("zhddkk:wsFeedback:remove")
	public Result<String> remove( Integer id){
	    WsFeedbackDO wsFeedbackDO = wsFeedbackService.selectById(id);
	    if (null != wsFeedbackDO) {
            wsFeedbackDO.setStatus(0);
            wsFeedbackService.updateById(wsFeedbackDO);
            return Result.ok();
        }
        return Result.fail();
	}
	
	/**
	 * 批量删除问题反馈
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	//@RequiresPermissions("zhddkk:wsFeedback:batchRemove")
	public Result<String> remove(@RequestParam("ids[]") Integer[] ids){
		wsFeedbackService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}

	@ResponseBody
    @PostMapping("/replyFeedback")
	public Result<String> replyFeedback(Integer id, String replyContext){
	    WsFeedbackDO wsFeedbackDO = wsFeedbackService.selectById(id);
	    if (null == wsFeedbackDO){
	        return Result.fail("记录不存在");
        }

        if (wsFeedbackDO.getStatus().intValue() != 1){
	        return Result.fail("记录不是待答复状态");
        }

        wsFeedbackDO.setStatus(2);
	    wsFeedbackDO.setReplyTime(new Date());
	    wsFeedbackDO.setReplyContent(replyContext);

	    boolean updateFlag = wsFeedbackService.updateById(wsFeedbackDO);
	    if (updateFlag){
	        return Result.ok();
        }

        return Result.fail();
    }
	
}

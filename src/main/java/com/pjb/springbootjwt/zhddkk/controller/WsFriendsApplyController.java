package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.pjb.springbootjwt.zhddkk.domain.WsFriendsApplyDO;
import com.pjb.springbootjwt.zhddkk.service.WsFriendsApplyService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 好友申请表
 */
@Controller
@RequestMapping("/zhddkk/wsFriendsApply")
public class WsFriendsApplyController extends AdminBaseController {

    private static Logger logger = LoggerFactory.getLogger(WsFriendsApplyController.class);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @Autowired
	private WsFriendsApplyService wsFriendsApplyService;


    /**
    * 我的申请页面
	*/
	@GetMapping("/myApply")
	String myApply(Model model, String user){
		model.addAttribute("user", user);
	    return "zhddkk/wsFriendsApply/myApply";
	}

    /**
     * 我的申请列表
     */
	@ResponseBody
	@GetMapping("/myApplyList")
	public Result<Page<WsFriendsApplyDO>> myApplyList(WsFriendsApplyDO wsFriendsApplyDTO){
        Wrapper<WsFriendsApplyDO> wrapper = new EntityWrapper<WsFriendsApplyDO>();
        if (StringUtils.isNotBlank(wsFriendsApplyDTO.getFromName())){
        	wrapper.eq("from_name", wsFriendsApplyDTO.getFromName());
		}
        wrapper.orderBy("create_time", false).orderBy("process_status", true);
        Page<WsFriendsApplyDO> page = wsFriendsApplyService.selectPage(getPage(WsFriendsApplyDO.class), wrapper);
        return Result.ok(page);
	}

	/**
	 * 好友申请页面
	 */
	@GetMapping("/friendApply")
	String friendApply(Model model, String user){
		model.addAttribute("user", user);
		return "myApply";
	}

	/**
	 * 好友申请列表
	 */
	@ResponseBody
	@GetMapping("/friendApplyList")
	public Result<Page<WsFriendsApplyDO>> friendApplyList(WsFriendsApplyDO wsFriendsApplyDTO){
		Wrapper<WsFriendsApplyDO> wrapper = new EntityWrapper<WsFriendsApplyDO>();
		Page<WsFriendsApplyDO> page = wsFriendsApplyService.selectPage(getPage(WsFriendsApplyDO.class), wrapper);
		return Result.ok(page);
	}
	
}

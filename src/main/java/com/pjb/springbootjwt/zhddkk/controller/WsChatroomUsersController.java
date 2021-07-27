package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.pjb.springbootjwt.zhddkk.domain.WsChatroomUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsChatroomUsersService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 聊天室人员信息.
 * 作者:admin
 * 邮箱:anonymous@anonymous.com
 * 创建时间:2021-07-27 09:42:47
 */
@Controller
@RequestMapping("/zhddkk/wsChatroomUsers")
public class WsChatroomUsersController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(WsChatroomUsersController.class);

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
	private WsChatroomUsersService wsChatroomUsersService;

    /**
    * 跳转到聊天室人员信息页面.
	*/
	@GetMapping()
	//@RequiresPermissions("zhddkk:wsChatroomUsers:wsChatroomUsers")
    public String wsChatroomUsers(){
	    return "zhddkk/wsChatroomUsers/wsChatroomUsers";
	}

    /**
     * 获取聊天室人员信息列表数据.
     */
	@ResponseBody
	@GetMapping("/list")
	//@RequiresPermissions("zhddkk:wsChatroomUsers:wsChatroomUsers")
	public Result<Page<WsChatroomUsersDO>> list(WsChatroomUsersDO wsChatroomUsersDto) {
        Wrapper<WsChatroomUsersDO> wrapper = new EntityWrapper<WsChatroomUsersDO>(wsChatroomUsersDto);
        Page<WsChatroomUsersDO> page = wsChatroomUsersService.selectPage(getPage(WsChatroomUsersDO.class), wrapper);
        return Result.ok(page);
	}

    /**
     * 跳转到聊天室人员信息添加页面.
     */
	@GetMapping("/add")
	//@RequiresPermissions("zhddkk:wsChatroomUsers:add")
    public String add(Model model) {
		WsChatroomUsersDO wsChatroomUsers = new WsChatroomUsersDO();
        model.addAttribute("wsChatroomUsers", wsChatroomUsers);
	    return "zhddkk/wsChatroomUsers/wsChatroomUsersForm";
	}

    /**
     * 跳转到聊天室人员信息编辑页面.
     * @param id 聊天室人员信息ID
     * @param model 聊天室人员信息实体
     */
	@GetMapping("/edit/{id}")
	//@RequiresPermissions("zhddkk:wsChatroomUsers:edit")
    public String edit(@PathVariable("id") Long id, Model model) {
		WsChatroomUsersDO wsChatroomUsers = wsChatroomUsersService.selectById(id);
		model.addAttribute("wsChatroomUsers", wsChatroomUsers);
	    return "zhddkk/wsChatroomUsers/wsChatroomUsersForm";
	}
	
	/**
	 * 保存聊天室人员信息.
	 */
	@ResponseBody
	@PostMapping("/save")
	//@RequiresPermissions("zhddkk:wsChatroomUsers:add")
	public Result<String> save(WsChatroomUsersDO wsChatroomUsers) {
		wsChatroomUsersService.insert(wsChatroomUsers);
        return Result.ok();
	}

	/**
	 * 编辑聊天室人员信息.
	 */
	@ResponseBody
	@RequestMapping("/update")
	//@RequiresPermissions("zhddkk:wsChatroomUsers:edit")
	public Result<String> update(WsChatroomUsersDO wsChatroomUsers) {
		wsChatroomUsersService.updateById(wsChatroomUsers);
		return Result.ok();
	}
	
	/**
	 * 删除聊天室人员信息.
	 */
	@PostMapping("/remove")
	@ResponseBody
	//@RequiresPermissions("zhddkk:wsChatroomUsers:remove")
	public Result<String> remove(Long id) {
		wsChatroomUsersService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 批量删除聊天室人员信息.
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	//@RequiresPermissions("zhddkk:wsChatroomUsers:batchRemove")
	public Result<String> remove(@RequestParam("ids[]") Long[] ids) {
		wsChatroomUsersService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}
}
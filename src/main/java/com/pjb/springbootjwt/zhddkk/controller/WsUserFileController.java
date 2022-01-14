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
import com.pjb.springbootjwt.zhddkk.domain.WsUserFileDO;
import com.pjb.springbootjwt.zhddkk.service.WsUserFileService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户文件表.
 * 作者:admin
 * 邮箱:anonymous@anonymous.com
 * 创建时间:2022-01-14 10:02:48
 */
@Controller
@RequestMapping("/zhddkk/wsUserFile")
public class WsUserFileController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(WsUserFileController.class);

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
	private WsUserFileService wsUserFileService;

    /**
    * 跳转到用户文件表页面.
	*/
	@GetMapping()
	//@RequiresPermissions("zhddkk:wsUserFile:wsUserFile")
    public String wsUserFile(){
	    return "zhddkk/wsUserFile/wsUserFile";
	}

    /**
     * 获取用户文件表列表数据.
     */
	@ResponseBody
	@GetMapping("/list")
	//@RequiresPermissions("zhddkk:wsUserFile:wsUserFile")
	public Result<Page<WsUserFileDO>> list(WsUserFileDO wsUserFileDto) {
        Wrapper<WsUserFileDO> wrapper = new EntityWrapper<WsUserFileDO>(wsUserFileDto);
        Page<WsUserFileDO> page = wsUserFileService.selectPage(getPage(WsUserFileDO.class), wrapper);
        return Result.ok(page);
	}

    /**
     * 跳转到用户文件表添加页面.
     */
	@GetMapping("/add")
	//@RequiresPermissions("zhddkk:wsUserFile:add")
    public String add(Model model) {
		WsUserFileDO wsUserFile = new WsUserFileDO();
        model.addAttribute("wsUserFile", wsUserFile);
	    return "zhddkk/wsUserFile/wsUserFileForm";
	}

    /**
     * 跳转到用户文件表编辑页面.
     * @param id 用户文件表ID
     * @param model 用户文件表实体
     */
	@GetMapping("/edit/{id}")
	//@RequiresPermissions("zhddkk:wsUserFile:edit")
    public String edit(@PathVariable("id") Long id, Model model) {
		WsUserFileDO wsUserFile = wsUserFileService.selectById(id);
		model.addAttribute("wsUserFile", wsUserFile);
	    return "zhddkk/wsUserFile/wsUserFileForm";
	}
	
	/**
	 * 保存用户文件表.
	 */
	@ResponseBody
	@PostMapping("/save")
	//@RequiresPermissions("zhddkk:wsUserFile:add")
	public Result<String> save(WsUserFileDO wsUserFile) {
		wsUserFileService.insert(wsUserFile);
        return Result.ok();
	}

	/**
	 * 编辑用户文件表.
	 */
	@ResponseBody
	@RequestMapping("/update")
	//@RequiresPermissions("zhddkk:wsUserFile:edit")
	public Result<String> update(WsUserFileDO wsUserFile) {
		wsUserFileService.updateById(wsUserFile);
		return Result.ok();
	}
	
	/**
	 * 删除用户文件表.
	 */
	@PostMapping("/remove")
	@ResponseBody
	//@RequiresPermissions("zhddkk:wsUserFile:remove")
	public Result<String> remove(Long id) {
		wsUserFileService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 批量删除用户文件表.
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	//@RequiresPermissions("zhddkk:wsUserFile:batchRemove")
	public Result<String> remove(@RequestParam("ids[]") Long[] ids) {
		wsUserFileService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}
}
package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import com.pjb.springbootjwt.zhddkk.domain.SysMenuDO;
import com.pjb.springbootjwt.zhddkk.service.SysMenuService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 菜单表.
 */
@Controller
@RequestMapping("/zhddkk/sysMenu")
public class SysMenuController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(SysMenuController.class);

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
	private SysMenuService sysMenuService;

    /**
    * 跳转到菜单表页面.
	*/
	@GetMapping()
	//@RequiresPermissions("zhddkk:sysMenu:sysMenu")
    public String sysMenu(){
	    return "zhddkk/sysMenu/sysMenu";
	}

    /**
     * 获取菜单表列表数据.
     */
	@ResponseBody
	@GetMapping("/list")
	//@RequiresPermissions("zhddkk:sysMenu:sysMenu")
	public List<SysMenuDO> list(SysMenuDO sysMenuDto) {
        Wrapper<SysMenuDO> wrapper = new EntityWrapper<SysMenuDO>(sysMenuDto);
        Page<SysMenuDO> page = sysMenuService.selectPage(getPage(SysMenuDO.class), wrapper);
        List<SysMenuDO> list = sysMenuService.selectList(null);
        return list;
	}

    /**
     * 跳转到菜单表添加页面.
     */
	@GetMapping("/add")
	//@RequiresPermissions("zhddkk:sysMenu:add")
    public String add(Model model) {
		SysMenuDO sysMenu = new SysMenuDO();
        model.addAttribute("sysMenu", sysMenu);

        List<SysMenuDO> pMenuList = sysMenuService.selectList(new EntityWrapper<SysMenuDO>().eq("parent_id", 0));
        model.addAttribute("pMenuList", pMenuList);

	    return "zhddkk/sysMenu/sysMenuForm";
	}

    /**
     * 跳转到菜单表编辑页面.
     * @param id 菜单表ID
     * @param model 菜单表实体
     */
	@GetMapping("/edit/{id}")
	//@RequiresPermissions("zhddkk:sysMenu:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
		SysMenuDO sysMenu = sysMenuService.selectById(id);
		model.addAttribute("sysMenu", sysMenu);
	    return "zhddkk/sysMenu/sysMenuForm";
	}
	
	/**
	 * 保存菜单表.
	 */
	@ResponseBody
	@PostMapping("/save")
	//@RequiresPermissions("zhddkk:sysMenu:add")
	public Result<String> save(SysMenuDO sysMenu) {
		sysMenuService.insert(sysMenu);
        return Result.ok();
	}

	/**
	 * 编辑菜单表.
	 */
	@ResponseBody
	@RequestMapping("/update")
	//@RequiresPermissions("zhddkk:sysMenu:edit")
	public Result<String> update(SysMenuDO sysMenu) {
		sysMenuService.updateById(sysMenu);
		return Result.ok();
	}
	
	/**
	 * 删除菜单表.
	 */
	@PostMapping("/remove")
	@ResponseBody
	//@RequiresPermissions("zhddkk:sysMenu:remove")
	public Result<String> remove(Integer id) {
		sysMenuService.deleteById(id);
        return Result.ok();
	}
	
	/**
	 * 批量删除菜单表.
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	//@RequiresPermissions("zhddkk:sysMenu:batchRemove")
	public Result<String> remove(@RequestParam("ids[]") Integer[] ids) {
		sysMenuService.deleteBatchIds(Arrays.asList(ids));
		return Result.ok();
	}
}
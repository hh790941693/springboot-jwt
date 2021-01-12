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
import com.pjb.springbootjwt.zhddkk.domain.SysRoleMenuDO;
import com.pjb.springbootjwt.zhddkk.service.SysRoleMenuService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 角色与菜单关系表.
 */
@Controller
@RequestMapping("/zhddkk/sysRoleMenu")
public class SysRoleMenuController extends AdminBaseController {
    
    private static final Logger logger = LoggerFactory.getLogger(SysRoleMenuController.class);
    
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
    private SysRoleMenuService sysRoleMenuService;
    
    /**
     * 跳转到角色与菜单关系表页面.
     */
    @GetMapping()
    // @RequiresPermissions("zhddkk:sysRoleMenu:sysRoleMenu")
    public String sysRoleMenu() {
        return "zhddkk/sysRoleMenu/sysRoleMenu";
    }
    
    /**
     * 获取角色与菜单关系表列表数据.
     */
    @ResponseBody
    @GetMapping("/list")
    // @RequiresPermissions("zhddkk:sysRoleMenu:sysRoleMenu")
    public Result<Page<SysRoleMenuDO>> list(SysRoleMenuDO sysRoleMenuDto) {
        Wrapper<SysRoleMenuDO> wrapper = new EntityWrapper<SysRoleMenuDO>(sysRoleMenuDto);
        Page<SysRoleMenuDO> page = sysRoleMenuService.selectPage(getPage(SysRoleMenuDO.class), wrapper);
        return Result.ok(page);
    }
    
    /**
     * 跳转到角色与菜单关系表添加页面.
     */
    @GetMapping("/add")
    // @RequiresPermissions("zhddkk:sysRoleMenu:add")
    public String add(Model model) {
        SysRoleMenuDO sysRoleMenu = new SysRoleMenuDO();
        model.addAttribute("sysRoleMenu", sysRoleMenu);
        return "zhddkk/sysRoleMenu/sysRoleMenuForm";
    }
    
    /**
     * 跳转到角色与菜单关系表编辑页面.
     * 
     * @param id 角色与菜单关系表ID
     * @param model 角色与菜单关系表实体
     */
    @GetMapping("/edit/{id}")
    // @RequiresPermissions("zhddkk:sysRoleMenu:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        SysRoleMenuDO sysRoleMenu = sysRoleMenuService.selectById(id);
        model.addAttribute("sysRoleMenu", sysRoleMenu);
        return "zhddkk/sysRoleMenu/sysRoleMenuForm";
    }
    
    /**
     * 保存角色与菜单关系表.
     */
    @ResponseBody
    @PostMapping("/save")
    // @RequiresPermissions("zhddkk:sysRoleMenu:add")
    public Result<String> save(SysRoleMenuDO sysRoleMenu) {
        sysRoleMenuService.insert(sysRoleMenu);
        return Result.ok();
    }
    
    /**
     * 编辑角色与菜单关系表.
     */
    @ResponseBody
    @RequestMapping("/update")
    // @RequiresPermissions("zhddkk:sysRoleMenu:edit")
    public Result<String> update(SysRoleMenuDO sysRoleMenu) {
        sysRoleMenuService.updateById(sysRoleMenu);
        return Result.ok();
    }
    
    /**
     * 删除角色与菜单关系表.
     */
    @PostMapping("/remove")
    @ResponseBody
    // @RequiresPermissions("zhddkk:sysRoleMenu:remove")
    public Result<String> remove(Integer id) {
        sysRoleMenuService.deleteById(id);
        return Result.ok();
    }
    
    /**
     * 批量删除角色与菜单关系表.
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    // @RequiresPermissions("zhddkk:sysRoleMenu:batchRemove")
    public Result<String> remove(@RequestParam("ids[]") Integer[] ids) {
        sysRoleMenuService.deleteBatchIds(Arrays.asList(ids));
        return Result.ok();
    }
}
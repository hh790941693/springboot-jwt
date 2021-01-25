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
import com.pjb.springbootjwt.zhddkk.domain.SysUserRoleDO;
import com.pjb.springbootjwt.zhddkk.service.SysUserRoleService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户与角色关系表.
 */
@Controller
@RequestMapping("/zhddkk/sysUserRole")
public class SysUserRoleController extends AdminBaseController {
    
    private static final Logger logger = LoggerFactory.getLogger(SysUserRoleController.class);
    
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
    private SysUserRoleService sysUserRoleService;
    
    /**
     * 跳转到用户与角色关系表页面.
     */
    @GetMapping()
    // @RequiresPermissions("zhddkk:sysUserRole:sysUserRole")
    public String sysUserRole() {
        return "zhddkk/sysUserRole/sysUserRole";
    }
    
    /**
     * 获取用户与角色关系表列表数据.
     */
    @ResponseBody
    @GetMapping("/list")
    // @RequiresPermissions("zhddkk:sysUserRole:sysUserRole")
    public Result<Page<SysUserRoleDO>> list(SysUserRoleDO sysUserRoleDto) {
        Wrapper<SysUserRoleDO> wrapper = new EntityWrapper<SysUserRoleDO>(sysUserRoleDto);
        Page<SysUserRoleDO> page = sysUserRoleService.selectPage(getPage(SysUserRoleDO.class), wrapper);
        return Result.ok(page);
    }
    
    /**
     * 跳转到用户与角色关系表添加页面.
     */
    @GetMapping("/add")
    // @RequiresPermissions("zhddkk:sysUserRole:add")
    public String add(Model model) {
        SysUserRoleDO sysUserRole = new SysUserRoleDO();
        model.addAttribute("sysUserRole", sysUserRole);
        return "zhddkk/sysUserRole/sysUserRoleForm";
    }
    
    /**
     * 跳转到用户与角色关系表编辑页面.
     * 
     * @param id 用户与角色关系表ID
     * @param model 用户与角色关系表实体
     */
    @GetMapping("/edit/{id}")
    // @RequiresPermissions("zhddkk:sysUserRole:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        SysUserRoleDO sysUserRole = sysUserRoleService.selectById(id);
        model.addAttribute("sysUserRole", sysUserRole);
        return "zhddkk/sysUserRole/sysUserRoleForm";
    }
    
    /**
     * 保存用户与角色关系表.
     */
    @ResponseBody
    @PostMapping("/save")
    // @RequiresPermissions("zhddkk:sysUserRole:add")
    public Result<String> save(SysUserRoleDO sysUserRole) {
        sysUserRoleService.insert(sysUserRole);
        return Result.ok();
    }
    
    /**
     * 编辑用户与角色关系表.
     */
    @ResponseBody
    @RequestMapping("/update")
    // @RequiresPermissions("zhddkk:sysUserRole:edit")
    public Result<String> update(SysUserRoleDO sysUserRole) {
        sysUserRoleService.updateById(sysUserRole);
        return Result.ok();
    }
    
    /**
     * 删除用户与角色关系表.
     */
    @PostMapping("/remove")
    @ResponseBody
    // @RequiresPermissions("zhddkk:sysUserRole:remove")
    public Result<String> remove(Integer id) {
        sysUserRoleService.deleteById(id);
        return Result.ok();
    }
    
    /**
     * 批量删除用户与角色关系表.
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    // @RequiresPermissions("zhddkk:sysUserRole:batchRemove")
    public Result<String> remove(@RequestParam("ids[]") Integer[] ids) {
        sysUserRoleService.deleteBatchIds(Arrays.asList(ids));
        return Result.ok();
    }
}
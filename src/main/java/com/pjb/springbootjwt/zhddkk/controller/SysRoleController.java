package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.enums.SqlLike;
import com.pjb.springbootjwt.zhddkk.domain.SysRoleMenuDO;
import com.pjb.springbootjwt.zhddkk.domain.SysUserRoleDO;
import com.pjb.springbootjwt.zhddkk.service.SysMenuService;
import com.pjb.springbootjwt.zhddkk.service.SysUserRoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.zhddkk.domain.SysRoleDO;
import com.pjb.springbootjwt.zhddkk.service.SysRoleService;
import com.pjb.springbootjwt.zhddkk.service.SysRoleMenuService;
import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 角色表.
 */
@Controller
@RequestMapping("/zhddkk/sysRole")
public class SysRoleController extends AdminBaseController {
    
    private static final Logger logger = LoggerFactory.getLogger(SysRoleController.class);
    
    @Autowired
    private SysRoleService sysRoleService;
    
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    
    @Autowired
    private SysUserRoleService sysUserRoleService;
    
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
    
    /**
     * 跳转到角色表页面.
     */
    @GetMapping()
    // @RequiresPermissions("zhddkk:sysRole:sysRole")
    public String sysRole() {
        return "zhddkk/sysRole/sysRole";
    }
    
    /**
     * 获取角色表列表数据.
     */
    @ResponseBody
    @GetMapping("/list")
    // @RequiresPermissions("zhddkk:sysRole:sysRole")
    public Result<Page<SysRoleDO>> list(SysRoleDO sysRoleDto) {
        Wrapper<SysRoleDO> wrapper = new EntityWrapper<SysRoleDO>(sysRoleDto);
        if (StringUtils.isNotBlank(sysRoleDto.getRoleNameLike())) {
            wrapper.like("name", sysRoleDto.getRoleNameLike(), SqlLike.DEFAULT);
        }
        Page<SysRoleDO> page = sysRoleService.selectPage(getPage(SysRoleDO.class), wrapper);
        return Result.ok(page);
    }
    
    /**
     * 跳转到角色表添加页面.
     */
    @GetMapping("/add")
    // @RequiresPermissions("zhddkk:sysRole:add")
    public String add(Model model) {
        SysRoleDO sysRole = new SysRoleDO();
        model.addAttribute("sysRole", sysRole);
        return "zhddkk/sysRole/sysRoleForm";
    }
    
    /**
     * 跳转到角色表编辑页面.
     * 
     * @param id 角色表ID
     * @param model 角色表实体
     */
    @GetMapping("/edit/{id}")
    // @RequiresPermissions("zhddkk:sysRole:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        SysRoleDO sysRole = sysRoleService.selectById(id);
        model.addAttribute("sysRole", sysRole);
        return "zhddkk/sysRole/sysRoleForm";
    }
    
    /**
     * 保存角色表.
     */
    @ResponseBody
    @PostMapping("/save")
    // @RequiresPermissions("zhddkk:sysRole:add")
    public Result<String> save(SysRoleDO sysRole) {
        String roleName = sysRole.getName();
        int roleCount = sysRoleService.selectCount(new EntityWrapper<SysRoleDO>().eq("name", roleName));
        if (roleCount > 0) {
            return Result.fail("角色名已存在");
        }
        sysRoleService.insert(sysRole);
        List<Integer> menuIdList = sysRole.getMenuIds();
        if (null != menuIdList && menuIdList.size() > 0) {
            List<SysRoleMenuDO> insertRoleMenuList = new ArrayList<>();
            for (int menuId : menuIdList) {
                SysRoleMenuDO sysRoleMenuDO = new SysRoleMenuDO();
                sysRoleMenuDO.setRoleId(sysRole.getId());
                sysRoleMenuDO.setRoleName(sysRole.getName());
                sysRoleMenuDO.setMenuId(menuId);
                insertRoleMenuList.add(sysRoleMenuDO);
            }
            sysRoleMenuService.removeByRoleId(sysRole.getId());
            sysRoleMenuService.batchSave(insertRoleMenuList);
        } else {
            sysRoleMenuService.removeByRoleId(sysRole.getId());
        }
        return Result.ok();
    }
    
    /**
     * 编辑角色表.
     */
    @ResponseBody
    @RequestMapping("/update")
    @Transactional
    // @RequiresPermissions("zhddkk:sysRole:edit")
    public Result<String> update(SysRoleDO sysRole) {
        sysRoleService.updateById(sysRole);
        List<Integer> menuIdList = sysRole.getMenuIds();
        if (null != menuIdList && menuIdList.size() > 0) {
            List<SysRoleMenuDO> insertRoleMenuList = new ArrayList<>();
            for (int menuId : menuIdList) {
                SysRoleMenuDO sysRoleMenuDO = new SysRoleMenuDO();
                sysRoleMenuDO.setRoleId(sysRole.getId());
                sysRoleMenuDO.setRoleName(sysRole.getName());
                sysRoleMenuDO.setMenuId(menuId);
                insertRoleMenuList.add(sysRoleMenuDO);
            }
            
            sysRoleMenuService.removeByRoleId(sysRole.getId());
            sysRoleMenuService.batchSave(insertRoleMenuList);
        }
        else {
            sysRoleMenuService.removeByRoleId(sysRole.getId());
        }
        return Result.ok();
    }
    
    /**
     * 删除角色表.
     */
    @PostMapping("/remove")
    @ResponseBody
    @Transactional
    // @RequiresPermissions("zhddkk:sysRole:remove")
    public Result<String> remove(Integer id) {
        SysRoleDO sysRoleDO = sysRoleService.selectById(id);
        if (sysRoleDO != null) {
            sysRoleService.deleteById(id);
            sysUserRoleService.delete(new EntityWrapper<SysUserRoleDO>().eq("role_id", id));
            sysRoleMenuService.delete(new EntityWrapper<SysRoleMenuDO>().eq("role_id", id));
            return Result.ok();
        }
        
        return Result.fail();
    }
    
    /**
     * 批量删除角色表.
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    // @RequiresPermissions("zhddkk:sysRole:batchRemove")
    public Result<String> remove(@RequestParam("ids[]") Integer[] ids) {
        sysRoleService.deleteBatchIds(Arrays.asList(ids));
        return Result.ok();
    }
}
package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.*;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.pjb.springbootjwt.zhddkk.bean.Tree;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.domain.SysRoleMenuDO;
import com.pjb.springbootjwt.zhddkk.domain.SysUserRoleDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.common.base.AdminBaseController;
import com.pjb.springbootjwt.zhddkk.domain.SysMenuDO;
import com.pjb.springbootjwt.zhddkk.service.SysMenuService;
import com.pjb.springbootjwt.zhddkk.service.SysUserRoleService;
import com.pjb.springbootjwt.zhddkk.service.SysRoleMenuService;
import com.pjb.springbootjwt.zhddkk.base.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * 菜单表.
 */
@Controller
@RequestMapping("/zhddkk/sysMenu")
public class SysMenuController extends AdminBaseController {
    
    private static final Logger logger = LoggerFactory.getLogger(SysMenuController.class);
    
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
    private SysMenuService sysMenuService;
    
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    
    @Autowired
    private WsUsersService wsUsersService;
    
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * messageSource.
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * 跳转到菜单表页面.
     */
    @GetMapping()
    // @RequiresPermissions("zhddkk:sysMenu:sysMenu")
    public String sysMenu() {
        return "zhddkk/sysMenu/sysMenu";
    }
    
    /**
     * 获取菜单表列表数据.
     */
    @ResponseBody
    @GetMapping("/list")
    // @RequiresPermissions("zhddkk:sysMenu:sysMenu")
    public List<SysMenuDO> list(SysMenuDO sysMenuDto) {
        return sysMenuService.selectList(null);
    }
    
    /**
     * 跳转到菜单表添加页面.
     */
    @GetMapping("/add")
    // @RequiresPermissions("zhddkk:sysMenu:add")
    public String add(Model model) {
        SysMenuDO sysMenu = new SysMenuDO();
        model.addAttribute("sysMenu", sysMenu);
        
        List<SysMenuDO> parentMenuList = sysMenuService.selectList(new EntityWrapper<SysMenuDO>().eq("parent_id", 0));
        model.addAttribute("pMenuList", parentMenuList);
        return "zhddkk/sysMenu/sysMenuForm";
    }
    
    /**
     * 跳转到菜单表编辑页面.
     * 
     * @param id 菜单表ID
     * @param model 菜单表实体
     */
    @GetMapping("/edit/{id}")
    // @RequiresPermissions("zhddkk:sysMenu:edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        SysMenuDO sysMenu = sysMenuService.selectById(id);
        model.addAttribute("sysMenu", sysMenu);

        List<SysMenuDO> parentMenuList = sysMenuService.selectList(new EntityWrapper<SysMenuDO>().eq("parent_id", 0));
        model.addAttribute("pMenuList", parentMenuList);
        return "zhddkk/sysMenu/sysMenuForm";
    }
    
    /**
     * 保存菜单表.
     */
    @ResponseBody
    @PostMapping("/save")
    // @RequiresPermissions("zhddkk:sysMenu:add")
    public Result<String> save(SysMenuDO sysMenu) {
        sysMenuService.insert(sysMenu);
        return Result.ok();
    }
    
    /**
     * 编辑菜单表.
     */
    @ResponseBody
    @RequestMapping("/update")
    // @RequiresPermissions("zhddkk:sysMenu:edit")
    public Result<String> update(SysMenuDO sysMenu) {
        sysMenuService.updateById(sysMenu);
        return Result.ok();
    }
    
    /**
     * 删除菜单表.
     */
    @PostMapping("/remove")
    @ResponseBody
    @Transactional
    // @RequiresPermissions("zhddkk:sysMenu:remove")
    public Result<String> remove(Integer id) {
        SysMenuDO sysMenuDO = sysMenuService.selectById(id);
        // 待删除的菜单id列表
        List<Integer> deleteMenuIdList = new ArrayList<>();
        deleteMenuIdList.add(id);
        
        if (null != sysMenuDO) {
            if (sysMenuDO.getParentId() == 0) {
                // 如果删除的是父节点,则同时删除其子节点菜单
                List<SysMenuDO> childrenMenuList =
                    sysMenuService.selectList(new EntityWrapper<SysMenuDO>().eq("parent_id", sysMenuDO.getId()));
                for (SysMenuDO smd : childrenMenuList) {
                    deleteMenuIdList.add(smd.getId());
                }
            }
            sysMenuService.deleteBatchIds(deleteMenuIdList);
            sysRoleMenuService.delete(new EntityWrapper<SysRoleMenuDO>().in("menu_id", deleteMenuIdList));

            return Result.ok();
        }
        return Result.fail();
    }
    
    /**
     * 批量删除菜单表.
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    // @RequiresPermissions("zhddkk:sysMenu:batchRemove")
    public Result<String> remove(@RequestParam("ids[]") Integer[] ids) {
        sysMenuService.deleteBatchIds(Arrays.asList(ids));
        return Result.ok();
    }
    
    /**
     * 跳转到选择图标页面.
     */
    @GetMapping("iconSelect.page")
    // @RequiresPermissions("zhddkk:sysMenu:sysMenu")
    public String iconSelect() {
        return "zhddkk/sysMenu/iconSelect";
    }
    
    @GetMapping("/tree")
    @ResponseBody
    Tree<SysMenuDO> tree() {
        Tree<SysMenuDO> tree = new Tree<SysMenuDO>();
        tree = sysMenuService.getTree();
        return tree;
    }
    
    @GetMapping("/tree/{roleId}")
    @ResponseBody
    Tree<SysMenuDO> tree(@PathVariable("roleId") int roleId) {
        Tree<SysMenuDO> tree = new Tree<SysMenuDO>();
        tree = sysMenuService.getTree(roleId);
        return tree;
    }
    
    /**
     * 查询角色对应的菜单列表.
     * 
     * @param userId 用户id
     * @return 角色菜单列表
     */
    @GetMapping("/getRoleMenuList")
    @ResponseBody
    public Result<List<SysMenuDO>> queryRoleMenuList(int userId, HttpServletRequest request) {
        List<SysMenuDO> targetList = new ArrayList<>();
        WsUsersDO wsUsersDO = wsUsersService.selectById(userId);
        if (null != wsUsersDO) {
            SysUserRoleDO sysUserRoleDO =
                sysUserRoleService.selectOne(new EntityWrapper<SysUserRoleDO>().eq("user_id", userId));
            if (null == sysUserRoleDO) {
                if (wsUsersDO.getName().equals(CommonConstants.ADMIN_USER)) {
                    SysMenuDO parentSysMenuDO = new SysMenuDO();
                    parentSysMenuDO.setName("系统管理");
                    parentSysMenuDO.setIcon("icon-menu-folder-open");
                    parentSysMenuDO.setI18nKey("li.pmenu.sysmanage.label");
                    
                    SysMenuDO sonSysMenuDO = new SysMenuDO();
                    sonSysMenuDO.setName("用户管理");
                    sonSysMenuDO.setIcon("icon icon-users");
                    sonSysMenuDO.setUrl("/zhddkk/wsUsers/wsUsersForAdmin");
                    sonSysMenuDO.setI18nKey("li.usermanage.label");
                    sonSysMenuDO.setExtColumn1("false");

                    List<SysMenuDO> childrenList = new ArrayList<SysMenuDO>();
                    childrenList.add(sonSysMenuDO);

                    parentSysMenuDO.setChildrenList(childrenList);
                    targetList.add(parentSysMenuDO);
                }
            } else {
                List<SysMenuDO> srcList = sysMenuService.queryRoleMenuList(sysUserRoleDO.getRoleId());
                Locale locale = LocaleContextHolder.getLocale();
                if (null == locale) {
                    locale = Locale.SIMPLIFIED_CHINESE;
                }
                for (SysMenuDO sysMenuDO : srcList) {
                    String i18nKey = sysMenuDO.getI18nKey();
                    if (StringUtils.isNotBlank(i18nKey)) {
                        String i18nValue = "";
                        try {
                            i18nValue = messageSource.getMessage(i18nKey, null, locale);
                        } catch (Exception e) {
                            System.out.println("解析i18nKey异常:" + i18nKey + " "+e.getMessage());
                            logger.error("解析i18nKey异常", e);
                        }
                        if (StringUtils.isNotBlank(i18nValue)) {
                            sysMenuDO.setName(i18nValue);
                        }
                    }
                }
                targetList = adjustMenuList(srcList);
            }
        }
        return Result.ok(targetList);
    }
    
    private static List<SysMenuDO> adjustMenuList(List<SysMenuDO> srcList) {
        List<SysMenuDO> parentList = new ArrayList<>();
        for (SysMenuDO sysMenuDO : srcList) {
            if (sysMenuDO.getParentId() == 0) {
                parentList.add(sysMenuDO);
            }
        }
        
        if (parentList.size() > 0) {
            for (SysMenuDO parentMenuDO : parentList) {
                List<SysMenuDO> childrenList = new ArrayList<>();
                for (SysMenuDO subMenuDO : srcList) {
                    if (subMenuDO.getParentId() == parentMenuDO.getId()) {
                        childrenList.add(subMenuDO);
                    }
                }
                parentMenuDO.setChildrenList(childrenList);
            }
        }
        
        return parentList;
    }
}
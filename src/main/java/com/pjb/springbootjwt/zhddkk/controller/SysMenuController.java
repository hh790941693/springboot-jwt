package com.pjb.springbootjwt.zhddkk.controller;

import java.text.SimpleDateFormat;
import java.util.*;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.pjb.springbootjwt.zhddkk.bean.LayuiTree;
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
 * ่ๅ่กจ.
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
     * ่ทณ่ฝฌๅฐ่ๅ่กจ้กต้ข.
     */
    @GetMapping()
    // @RequiresPermissions("zhddkk:sysMenu:sysMenu")
    public String sysMenu() {
        return "zhddkk/sysMenu/sysMenu";
    }
    
    /**
     * ่ทๅ่ๅ่กจๅ่กจๆฐๆฎ.
     */
    @ResponseBody
    @GetMapping("/list")
    // @RequiresPermissions("zhddkk:sysMenu:sysMenu")
    public List<SysMenuDO> list(SysMenuDO sysMenuDto) {
        return sysMenuService.selectList(new EntityWrapper<SysMenuDO>().orderBy("order_num",true));
    }
    
    /**
     * ่ทณ่ฝฌๅฐ่ๅ่กจๆทปๅ?้กต้ข.
     */
    @GetMapping("/add")
    // @RequiresPermissions("zhddkk:sysMenu:add")
    public String add(Model model, int pid) {
        SysMenuDO sysMenu = new SysMenuDO();
        sysMenu.setParentId(pid);
        model.addAttribute("sysMenu", sysMenu);
        
        List<SysMenuDO> parentMenuList = sysMenuService.selectList(new EntityWrapper<SysMenuDO>().eq("parent_id", 0));
        model.addAttribute("pMenuList", parentMenuList);
        return "zhddkk/sysMenu/sysMenuForm";
    }
    
    /**
     * ่ทณ่ฝฌๅฐ่ๅ่กจ็ผ่พ้กต้ข.
     * 
     * @param id ่ๅ่กจID
     * @param model ่ๅ่กจๅฎไฝ
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
     * ไฟๅญ่ๅ่กจ.
     */
    @ResponseBody
    @PostMapping("/save")
    // @RequiresPermissions("zhddkk:sysMenu:add")
    public Result<String> save(SysMenuDO sysMenu) {
        sysMenuService.insert(sysMenu);
        return Result.ok();
    }
    
    /**
     * ็ผ่พ่ๅ่กจ.
     */
    @ResponseBody
    @RequestMapping("/update")
    // @RequiresPermissions("zhddkk:sysMenu:edit")
    public Result<String> update(SysMenuDO sysMenu) {
        sysMenuService.updateById(sysMenu);
        return Result.ok();
    }
    
    /**
     * ๅ?้ค่ๅ่กจ.
     */
    @PostMapping("/remove")
    @ResponseBody
    @Transactional
    // @RequiresPermissions("zhddkk:sysMenu:remove")
    public Result<String> remove(Integer id) {
        SysMenuDO sysMenuDO = sysMenuService.selectById(id);
        // ๅพๅ?้ค็่ๅidๅ่กจ
        List<Integer> deleteMenuIdList = new ArrayList<>();
        deleteMenuIdList.add(id);
        
        if (null != sysMenuDO) {
            if (sysMenuDO.getParentId() == 0) {
                // ๅฆๆๅ?้ค็ๆฏ็ถ่็น,ๅๅๆถๅ?้คๅถๅญ่็น่ๅ
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
     * ๆน้ๅ?้ค่ๅ่กจ.
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    // @RequiresPermissions("zhddkk:sysMenu:batchRemove")
    public Result<String> remove(@RequestParam("ids[]") Integer[] ids) {
        sysMenuService.deleteBatchIds(Arrays.asList(ids));
        return Result.ok();
    }
    
    /**
     * ่ทณ่ฝฌๅฐ้ๆฉๅพๆ?้กต้ข.
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
     * ๆฅ่ฏข่ง่ฒๅฏนๅบ็่ๅๅ่กจ.
     * 
     * @param userId ็จๆทid
     * @return ่ง่ฒ่ๅๅ่กจ
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
                    parentSysMenuDO.setName("็ณป็ป็ฎก็");
                    parentSysMenuDO.setIcon("icon-menu-folder-open");
                    parentSysMenuDO.setI18nKey("li.pmenu.sysmanage.label");
                    parentSysMenuDO.setOrderNum(1);
                    
                    SysMenuDO sonSysMenuDO = new SysMenuDO();
                    sonSysMenuDO.setName("็จๆท็ฎก็");
                    sonSysMenuDO.setIcon("icon icon-users");
                    sonSysMenuDO.setUrl("/zhddkk/wsUsers/wsUsersForAdmin");
                    sonSysMenuDO.setI18nKey("li.usermanage.label");
                    sonSysMenuDO.setExtColumn1("false");
                    sonSysMenuDO.setOrderNum(1);

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
                            System.out.println("่งฃๆi18nKeyๅผๅธธ:" + i18nKey + " "+e.getMessage());
                            logger.error("่งฃๆi18nKeyๅผๅธธ", e);
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

    @GetMapping("/layuiTree")
    @ResponseBody
    LayuiTree<SysMenuDO> layuiTree() {
        return sysMenuService.getLayuiTree();
    }

    @GetMapping("/layuiTree/{roleId}")
    @ResponseBody
    LayuiTree<SysMenuDO> layuiTree(@PathVariable("roleId") int roleId) {
        return sysMenuService.getLayuiTree(roleId);
    }
}
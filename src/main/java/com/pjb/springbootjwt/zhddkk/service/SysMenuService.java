package com.pjb.springbootjwt.zhddkk.service;

import com.pjb.springbootjwt.zhddkk.bean.LayuiTree;
import com.pjb.springbootjwt.zhddkk.bean.Tree;
import com.pjb.springbootjwt.zhddkk.domain.SysMenuDO;
import com.pjb.springbootjwt.zhddkk.base.CoreService;

import java.util.List;

/**
 * 菜单表.
 */

public interface SysMenuService extends CoreService<SysMenuDO> {

    Tree<SysMenuDO> getTree();

    Tree<SysMenuDO> getTree(int roleId);

    LayuiTree<SysMenuDO> getLayuiTree();

    LayuiTree<SysMenuDO> getLayuiTree(int roleId);

    List<SysMenuDO> queryRoleMenuList(int roleId);
}

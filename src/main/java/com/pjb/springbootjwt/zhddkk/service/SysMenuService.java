package com.pjb.springbootjwt.zhddkk.service;

import com.pjb.springbootjwt.zhddkk.bean.Tree;
import com.pjb.springbootjwt.zhddkk.domain.SysMenuDO;
import com.pjb.springbootjwt.zhddkk.base.CoreService;

/**
 * 菜单表.
 */

public interface SysMenuService extends CoreService<SysMenuDO> {

    Tree<SysMenuDO> getTree();

    Tree<SysMenuDO> getTree(int id);
}

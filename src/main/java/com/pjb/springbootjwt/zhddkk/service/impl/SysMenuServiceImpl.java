package com.pjb.springbootjwt.zhddkk.service.impl;

import com.pjb.springbootjwt.zhddkk.bean.BuildLayuiTree;
import com.pjb.springbootjwt.zhddkk.bean.BuildTree;
import com.pjb.springbootjwt.zhddkk.bean.LayuiTree;
import com.pjb.springbootjwt.zhddkk.bean.Tree;
import com.pjb.springbootjwt.zhddkk.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.SysMenuDao;
import com.pjb.springbootjwt.zhddkk.domain.SysMenuDO;
import com.pjb.springbootjwt.zhddkk.service.SysMenuService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单表.
 */
@Service
public class SysMenuServiceImpl extends CoreServiceImpl<SysMenuDao, SysMenuDO> implements SysMenuService {

    private static final Logger logger = LoggerFactory.getLogger(SysMenuServiceImpl.class);

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public Tree<SysMenuDO> getTree() {
        List<Tree<SysMenuDO>> trees = new ArrayList<Tree<SysMenuDO>>();
        List<SysMenuDO> menuDOs = baseMapper.selectList(null);
        for (SysMenuDO sysMenuDO : menuDOs) {
            Tree<SysMenuDO> tree = new Tree<SysMenuDO>();
            tree.setId(sysMenuDO.getId().toString());
            tree.setParentId(sysMenuDO.getParentId().toString());
            tree.setText(sysMenuDO.getName());
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<SysMenuDO> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public Tree<SysMenuDO> getTree(int id) {
        // 根据roleId查询权限
        List<SysMenuDO> menus = baseMapper.selectList(null);
        List<Integer> menuIds = sysRoleMenuService.listMenuIdByRoleId(id);
        List<Integer> temp = menuIds;
        for (SysMenuDO menu : menus) {
            if (temp.contains(menu.getParentId())) {
                menuIds.remove(menu.getParentId());
            }
        }
        List<Tree<SysMenuDO>> trees = new ArrayList<Tree<SysMenuDO>>();
        List<SysMenuDO> menuDOs = baseMapper.selectList(null);
        for (SysMenuDO sysMenuDO : menuDOs) {
            Tree<SysMenuDO> tree = new Tree<SysMenuDO>();
            tree.setId(sysMenuDO.getId().toString());
            tree.setParentId(sysMenuDO.getParentId().toString());
            tree.setText(sysMenuDO.getName());
            Map<String, Object> state = new HashMap<>(16);
            int menuId = sysMenuDO.getId();
            if (menuIds.contains(menuId)) {
                state.put("selected", true);
            } else {
                state.put("selected", false);
            }
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<SysMenuDO> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public LayuiTree<SysMenuDO> getLayuiTree() {
        List<LayuiTree<SysMenuDO>> trees = new ArrayList<LayuiTree<SysMenuDO>>();
        List<SysMenuDO> menuDOs = baseMapper.selectList(null);
        for (SysMenuDO sysMenuDO : menuDOs) {
            LayuiTree<SysMenuDO> tree = new LayuiTree<SysMenuDO>();
            tree.setId(sysMenuDO.getId().toString());
            tree.setParentId(sysMenuDO.getParentId().toString());
            tree.setTitle(sysMenuDO.getName());
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        LayuiTree<SysMenuDO> t = BuildLayuiTree.buildTree(trees);
        return t;
    }

    @Override
    public LayuiTree<SysMenuDO> getLayuiTree(int id) {
        // 根据roleId查询权限
        List<SysMenuDO> menus = baseMapper.selectList(null);
        List<Integer> menuIds = sysRoleMenuService.listMenuIdByRoleId(id);
        List<Integer> temp = menuIds;
        for (SysMenuDO menu : menus) {
            if (temp.contains(menu.getParentId())) {
                menuIds.remove(menu.getParentId());
            }
        }
        List<LayuiTree<SysMenuDO>> trees = new ArrayList<LayuiTree<SysMenuDO>>();
        List<SysMenuDO> menuDOs = baseMapper.selectList(null);
        for (SysMenuDO sysMenuDO : menuDOs) {
            LayuiTree<SysMenuDO> tree = new LayuiTree<SysMenuDO>();
            tree.setId(sysMenuDO.getId().toString());
            tree.setParentId(sysMenuDO.getParentId().toString());
            tree.setTitle(sysMenuDO.getName());
            Map<String, Object> state = new HashMap<>(16);
            int menuId = sysMenuDO.getId();
            if (menuIds.contains(menuId)) {
                tree.setChecked(true);
            } else {
                tree.setChecked(false);
            }
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        LayuiTree<SysMenuDO> t = BuildLayuiTree.buildTree(trees);
        return t;
    }

    @Override
    public List<SysMenuDO> queryRoleMenuList(int roleId) {
        return baseMapper.queryRoleMenuList(roleId);
    }
}

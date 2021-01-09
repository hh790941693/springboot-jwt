package com.pjb.springbootjwt.zhddkk.service;

import com.pjb.springbootjwt.zhddkk.domain.SysRoleMenuDO;
import com.pjb.springbootjwt.zhddkk.base.CoreService;

import java.util.List;

/**
 * 角色与菜单关系表.
 */
public interface SysRoleMenuService extends CoreService<SysRoleMenuDO> {
    List<Integer> listMenuIdByRoleId(int roleId);

    void removeByRoleId(int roleId);

    void batchSave(List<SysRoleMenuDO> list);
}

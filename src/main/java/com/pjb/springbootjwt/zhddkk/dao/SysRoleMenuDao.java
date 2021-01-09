package com.pjb.springbootjwt.zhddkk.dao;

import com.pjb.springbootjwt.zhddkk.domain.SysRoleMenuDO;
import com.pjb.springbootjwt.zhddkk.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 角色与菜单关系表
 */
@Repository
public interface SysRoleMenuDao extends BaseDao<SysRoleMenuDO> {

    List<Integer> listMenuIdByRoleId(int roleId);

    int removeByRoleId(int roleId);

    int batchSave(List<SysRoleMenuDO> list);
}

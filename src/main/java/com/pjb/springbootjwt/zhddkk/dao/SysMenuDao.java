package com.pjb.springbootjwt.zhddkk.dao;

import com.pjb.springbootjwt.zhddkk.domain.SysMenuDO;
import com.pjb.springbootjwt.zhddkk.base.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单表
 */
@Repository
public interface SysMenuDao extends BaseDao<SysMenuDO> {
    List<SysMenuDO> queryRoleMenuList(@Param("roleId")int roleId);
}

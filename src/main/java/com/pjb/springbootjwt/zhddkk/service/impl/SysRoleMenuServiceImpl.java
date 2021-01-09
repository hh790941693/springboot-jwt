package com.pjb.springbootjwt.zhddkk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.SysRoleMenuDao;
import com.pjb.springbootjwt.zhddkk.domain.SysRoleMenuDO;
import com.pjb.springbootjwt.zhddkk.service.SysRoleMenuService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 角色与菜单关系表.
 */
@Service
public class SysRoleMenuServiceImpl extends CoreServiceImpl<SysRoleMenuDao, SysRoleMenuDO> implements SysRoleMenuService {

    private static final Logger logger = LoggerFactory.getLogger(SysRoleMenuServiceImpl.class);

    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;

    @Override
    public List<Integer> listMenuIdByRoleId(int roleId) {
        return sysRoleMenuDao.listMenuIdByRoleId(roleId);
    }

    @Override
    public void removeByRoleId(int roleId) {
        sysRoleMenuDao.removeByRoleId(roleId);
    }

    @Override
    public void batchSave(List<SysRoleMenuDO> list) {
        sysRoleMenuDao.batchSave(list);
    }
}

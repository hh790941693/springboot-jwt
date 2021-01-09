package com.pjb.springbootjwt.zhddkk.service.impl;

import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.SysUserRoleDao;
import com.pjb.springbootjwt.zhddkk.domain.SysUserRoleDO;
import com.pjb.springbootjwt.zhddkk.service.SysUserRoleService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户与角色关系表.
 */
@Service
public class SysUserRoleServiceImpl extends CoreServiceImpl<SysUserRoleDao, SysUserRoleDO> implements SysUserRoleService {

    private static final Logger logger = LoggerFactory.getLogger(SysUserRoleServiceImpl.class);
}

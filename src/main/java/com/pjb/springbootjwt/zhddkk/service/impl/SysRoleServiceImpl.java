package com.pjb.springbootjwt.zhddkk.service.impl;

import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.SysRoleDao;
import com.pjb.springbootjwt.zhddkk.domain.SysRoleDO;
import com.pjb.springbootjwt.zhddkk.service.SysRoleService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 角色表.
 */
@Service
public class SysRoleServiceImpl extends CoreServiceImpl<SysRoleDao, SysRoleDO> implements SysRoleService {

    private static final Logger logger = LoggerFactory.getLogger(SysRoleServiceImpl.class);
}

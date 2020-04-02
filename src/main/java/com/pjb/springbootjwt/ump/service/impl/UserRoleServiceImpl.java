package com.pjb.springbootjwt.ump.service.impl;

import com.pjb.springbootjwt.common.base.CoreServiceImpl;
import com.pjb.springbootjwt.ump.dao.UserRoleDao;
import com.pjb.springbootjwt.ump.domain.UserRoleDO;
import com.pjb.springbootjwt.ump.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends CoreServiceImpl<UserRoleDao, UserRoleDO> implements UserRoleService {
}

package com.pjb.springbootjwt.ump.service.impl;

import com.pjb.springbootjwt.common.base.CoreServiceImpl;
import com.pjb.springbootjwt.ump.dao.RoleDao;
import com.pjb.springbootjwt.ump.domain.RoleDO;
import com.pjb.springbootjwt.ump.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends CoreServiceImpl<RoleDao, RoleDO> implements RoleService {

}

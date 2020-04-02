package com.pjb.springbootjwt.ump.service.impl;

import com.pjb.springbootjwt.common.base.CoreServiceImpl;
import com.pjb.springbootjwt.ump.dao.RolePermissionDao;
import com.pjb.springbootjwt.ump.domain.RolePermissionDO;
import com.pjb.springbootjwt.ump.service.RolePermissionService;
import org.springframework.stereotype.Service;

@Service
public class RolePermissionServiceImpl extends CoreServiceImpl<RolePermissionDao, RolePermissionDO> implements RolePermissionService {
}

package com.pjb.springbootjwt.ump.service.impl;

import com.pjb.springbootjwt.common.base.CoreServiceImpl;
import com.pjb.springbootjwt.ump.dao.PermissionDao;
import com.pjb.springbootjwt.ump.domain.PermissionDO;
import com.pjb.springbootjwt.ump.service.PermissionService;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl extends CoreServiceImpl<PermissionDao, PermissionDO> implements PermissionService {
}

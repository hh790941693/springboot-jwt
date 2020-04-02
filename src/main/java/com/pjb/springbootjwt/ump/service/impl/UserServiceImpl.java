package com.pjb.springbootjwt.ump.service.impl;

import com.pjb.springbootjwt.common.base.CoreServiceImpl;
import com.pjb.springbootjwt.ump.domain.PermissionDO;
import com.pjb.springbootjwt.ump.domain.RoleDO;
import com.pjb.springbootjwt.ump.domain.UserDO;
import com.pjb.springbootjwt.ump.dao.UserDao;
import com.pjb.springbootjwt.ump.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends CoreServiceImpl<UserDao, UserDO> implements UserService {

    public UserDO findByUsername(String username){
        return baseMapper.findByUsername(username);
    }

    public UserDO findUserById(String userId) {
        return baseMapper.findUserById(userId);
    }

    public List<RoleDO> selectUserRoles(String userId){
        return baseMapper.selectUserRoles(userId);
    }
    public List<PermissionDO> selectUserPermissions(String userId){
        return baseMapper.selectUserPermissions(userId);
    }
}

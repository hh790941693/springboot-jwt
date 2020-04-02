package com.pjb.springbootjwt.ump.service;

import com.pjb.springbootjwt.common.base.CoreService;
import com.pjb.springbootjwt.ump.domain.PermissionDO;
import com.pjb.springbootjwt.ump.domain.RoleDO;
import com.pjb.springbootjwt.ump.domain.UserDO;

import java.util.List;

/**
 * @author jinbin
 * @date 2018-07-08 20:52
 */
public interface UserService extends CoreService<UserDO> {
    UserDO findByUsername(String username);
    UserDO findUserById(String userId);

    List<RoleDO> selectUserRoles(String userId);
    List<PermissionDO> selectUserPermissions(String userId);
}

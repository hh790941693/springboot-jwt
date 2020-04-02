package com.pjb.springbootjwt.ump.dao;

import com.pjb.springbootjwt.common.base.BaseDao;
import com.pjb.springbootjwt.ump.domain.PermissionDO;
import com.pjb.springbootjwt.ump.domain.RoleDO;
import com.pjb.springbootjwt.ump.domain.UserDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jinbin
 * @date 2018-07-08 20:44
 */
@Repository
public interface UserDao extends BaseDao<UserDO> {
    UserDO findByUsername(@Param("username") String username);
    UserDO findUserById(@Param("userId") String userId);

    List<RoleDO> selectUserRoles(@Param("userId") String userId);
    List<PermissionDO> selectUserPermissions(@Param("userId") String userId);
}

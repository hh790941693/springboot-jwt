package com.pjb.springbootjwt.common.shiro;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.ump.domain.PermissionDO;
import com.pjb.springbootjwt.ump.domain.RoleDO;
import com.pjb.springbootjwt.ump.domain.UserDO;
import com.pjb.springbootjwt.ump.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CustomRealm extends AuthorizingRealm {

    private static Logger logger = LoggerFactory.getLogger(CustomRealm.class);

    @Autowired
    private UserService userService;

    /**
     * 权限认证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("进入CustomRealm.doGetAuthorizationInfo");
        //获取登录用户名
        UserDO principal = (UserDO) principalCollection.getPrimaryPrincipal();
        UserDO userDO = userService.selectById(principal.getId());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //根据用户名去数据库查询用户信息
        List<RoleDO> roleList = userService.selectUserRoles(userDO.getId());
        //添加角色
        if (roleList.size()>0) {
            logger.info("用户角色列表:{}", roleList);
            for (RoleDO roleDO : roleList) {
                simpleAuthorizationInfo.addRole(roleDO.getRolename());
            }
        }
        //添加权限
        List<PermissionDO> permissionList = userService.selectUserPermissions(userDO.getId());
        if (permissionList.size()>0) {
            logger.info("用户权限列表:{}", permissionList);
            for (PermissionDO permissionDO : permissionList) {
                simpleAuthorizationInfo.addStringPermission(permissionDO.getPermissionName());
            }
        }
        logger.info("退出CustomRealm.doGetAuthorizationInfo");
        return simpleAuthorizationInfo;
    }

    /**
     * 身份认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("进入CustomRealm.doGetAuthenticationInfo");
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }

        String username = (String)authenticationToken.getPrincipal();
        String password = new String((char[])authenticationToken.getCredentials());
        UserDO userDO = userService.selectOne(new EntityWrapper<UserDO>().eq("username", username));
        if (null == userDO){
            logger.info("用户{}不存在", username);
            return null;
        }
        if (!password.equals(userDO.getPassword())){
            logger.info("用户{}密码错误", username);
            return null;
        }

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userDO, userDO.getPassword().toString(), getName());
        logger.info("验证通过CustomRealm.doGetAuthenticationInfo");
        return simpleAuthenticationInfo;
    }
}

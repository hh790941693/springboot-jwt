package com.pjb.springbootjwt.zhddkk.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.pjb.springbootjwt.zhddkk.base.BaseDao;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户账号表.
 */
@Repository
public interface WsUsersDao extends BaseDao<WsUsersDO> {

    List<WsUsersDO> queryUserPage(RowBounds rowBounds, @Param("ew") Wrapper<WsUsersDO> wrapper);

    List<WsUsersDO> queryMyFriendList(@Param("userId") Integer userId);

    @Select("select t1.*,t2.img AS head_image,t2.sign,IFNULL(t3.role_id, '') AS role_id,IFNULL(t3.role_name, '') AS role_name from ws_users t1 left join ws_user_profile t2 ON t1.id = t2.user_id LEFT JOIN sys_user_role t3 ON t1.id = t3.user_id  where binary t1.name=#{name}")
    WsUsersDO queryUserByName(@Param("name")String name);
}

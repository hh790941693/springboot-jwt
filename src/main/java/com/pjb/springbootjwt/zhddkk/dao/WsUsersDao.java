package com.pjb.springbootjwt.zhddkk.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.pjb.springbootjwt.zhddkk.base.BaseDao;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户账号表
 */
@Repository
public interface WsUsersDao extends BaseDao<WsUsersDO> {

    List<WsUsersDO> queryUserPage(RowBounds rowBounds, @Param("ew") Wrapper<WsUsersDO> wrapper);
}

package com.pjb.springbootjwt.zhddkk.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.pjb.springbootjwt.zhddkk.base.BaseDao;
import com.pjb.springbootjwt.zhddkk.domain.WsFriendsDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 好友列表.
 */
@Repository
public interface WsFriendsDao extends BaseDao<WsFriendsDO> {
    List<WsFriendsDO> queryFriendsPage(RowBounds rowBounds, @Param("ew") Wrapper<WsFriendsDO> wrapper);
}

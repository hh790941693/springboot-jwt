package com.pjb.springbootjwt.zhddkk.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.zhddkk.base.CoreService;
import com.pjb.springbootjwt.zhddkk.domain.WsFriendsDO;

import java.util.List;

/**
 * 好友列表.
 */
public interface WsFriendsService extends CoreService<WsFriendsDO> {
    List<WsFriendsDO> queryFriendsPage(Page<WsFriendsDO> page, Wrapper<WsFriendsDO> wrapper);
}

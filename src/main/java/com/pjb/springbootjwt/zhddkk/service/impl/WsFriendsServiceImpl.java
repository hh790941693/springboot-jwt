package com.pjb.springbootjwt.zhddkk.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsFriendsDao;
import com.pjb.springbootjwt.zhddkk.domain.WsFriendsDO;
import com.pjb.springbootjwt.zhddkk.service.WsFriendsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 好友列表
 */
@Service
public class WsFriendsServiceImpl extends CoreServiceImpl<WsFriendsDao, WsFriendsDO> implements WsFriendsService {

    private static Logger logger = LoggerFactory.getLogger(WsFriendsServiceImpl.class);

    @Override
    public List<WsFriendsDO> queryFriendsPage(Page<WsFriendsDO> page, Wrapper<WsFriendsDO> wrapper) {
        return this.baseMapper.queryFriendsPage(page, wrapper);
    }
}

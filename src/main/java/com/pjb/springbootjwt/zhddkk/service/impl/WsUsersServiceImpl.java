package com.pjb.springbootjwt.zhddkk.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsUsersDao;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 用户账号表
 */
@Service
public class WsUsersServiceImpl extends CoreServiceImpl<WsUsersDao, WsUsersDO> implements WsUsersService {

    private static Logger logger = LoggerFactory.getLogger(WsUsersServiceImpl.class);

    @Override
    public List<WsUsersDO> queryUserPage(Page<WsUsersDO> page, Wrapper<WsUsersDO> wrapper) {
        return this.baseMapper.queryUserPage(page, wrapper);
    }

    @Override
    public List<WsUsersDO> queryMyFriendList(Integer userId) {
        return this.baseMapper.queryMyFriendList(userId);
    }

    @Override
    public WsUsersDO queryUserByName(String name) {
        return this.baseMapper.queryUserByName(name);
    }
}

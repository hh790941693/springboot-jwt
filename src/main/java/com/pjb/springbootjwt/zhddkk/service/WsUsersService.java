package com.pjb.springbootjwt.zhddkk.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.zhddkk.base.CoreService;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;

import java.util.List;

/**
 * 用户账号表
 */
public interface WsUsersService extends CoreService<WsUsersDO> {

    List<WsUsersDO> queryUserPage(Page<WsUsersDO> page, Wrapper<WsUsersDO> wrapper);
}

package com.pjb.springbootjwt.zhddkk.service.impl;

import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsChatroomUsersDao;
import com.pjb.springbootjwt.zhddkk.domain.WsChatroomUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsChatroomUsersService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 聊天室人员信息.
 * 作者:admin
 * 邮箱:anonymous@anonymous.com
 * 创建时间:2021-07-27 09:42:47
 */
@Service
public class WsChatroomUsersServiceImpl extends CoreServiceImpl<WsChatroomUsersDao, WsChatroomUsersDO> implements WsChatroomUsersService {

    private static final Logger logger = LoggerFactory.getLogger(WsChatroomUsersServiceImpl.class);
}

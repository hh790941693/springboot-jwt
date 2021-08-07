package com.pjb.springbootjwt.zhddkk.service.impl;

import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsUserSessionDao;
import com.pjb.springbootjwt.zhddkk.domain.WsUserSessionDO;
import com.pjb.springbootjwt.zhddkk.service.WsUserSessionService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户会话表.
 * 作者:admin
 * 邮箱:547495788@qq.com
 * 创建时间:2021-08-07 17:38:43
 */
@Service
public class WsUserSessionServiceImpl extends CoreServiceImpl<WsUserSessionDao, WsUserSessionDO> implements WsUserSessionService {

    private static final Logger logger = LoggerFactory.getLogger(WsUserSessionServiceImpl.class);
}

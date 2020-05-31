package com.pjb.springbootjwt.zhddkk.service.impl;

import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsSignDao;
import com.pjb.springbootjwt.zhddkk.domain.WsSignDO;
import com.pjb.springbootjwt.zhddkk.service.WsSignService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户签到表
 */
@Service
public class WsSignServiceImpl extends CoreServiceImpl<WsSignDao, WsSignDO> implements WsSignService {

    private static Logger logger = LoggerFactory.getLogger(WsSignServiceImpl.class);
}

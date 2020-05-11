package com.pjb.springbootjwt.zhddkk.service.impl;

import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsCircleDao;
import com.pjb.springbootjwt.zhddkk.domain.WsCircleDO;
import com.pjb.springbootjwt.zhddkk.service.WsCircleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
@Service
public class WsCircleServiceImpl extends CoreServiceImpl<WsCircleDao, WsCircleDO> implements WsCircleService {

    private static Logger logger = LoggerFactory.getLogger(WsCircleServiceImpl.class);
}

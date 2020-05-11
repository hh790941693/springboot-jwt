package com.pjb.springbootjwt.zhddkk.service.impl;

import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsUserProfileDao;
import com.pjb.springbootjwt.zhddkk.domain.WsUserProfileDO;
import com.pjb.springbootjwt.zhddkk.service.WsUserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
@Service
public class WsUserProfileServiceImpl extends CoreServiceImpl<WsUserProfileDao, WsUserProfileDO> implements WsUserProfileService {

    private static Logger logger = LoggerFactory.getLogger(WsUserProfileServiceImpl.class);
}

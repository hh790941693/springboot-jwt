package com.pjb.springbootjwt.zhddkk.service.impl;

import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsCommonDao;
import com.pjb.springbootjwt.zhddkk.domain.WsCommonDO;
import com.pjb.springbootjwt.zhddkk.service.WsCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
@Service
public class WsCommonServiceImpl extends CoreServiceImpl<WsCommonDao, WsCommonDO> implements WsCommonService {

    private static Logger logger = LoggerFactory.getLogger(WsCommonServiceImpl.class);
}

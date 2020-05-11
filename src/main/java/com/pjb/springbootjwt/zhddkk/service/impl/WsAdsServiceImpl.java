package com.pjb.springbootjwt.zhddkk.service.impl;

import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsAdsDao;
import com.pjb.springbootjwt.zhddkk.domain.WsAdsDO;
import com.pjb.springbootjwt.zhddkk.service.WsAdsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 广告表
 */
@Service
public class WsAdsServiceImpl extends CoreServiceImpl<WsAdsDao, WsAdsDO> implements WsAdsService {

    private static Logger logger = LoggerFactory.getLogger(WsAdsServiceImpl.class);
}

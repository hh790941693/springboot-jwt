package com.pjb.springbootjwt.zhddkk.service.impl;

import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsChatlogDao;
import com.pjb.springbootjwt.zhddkk.domain.WsChatlogDO;
import com.pjb.springbootjwt.zhddkk.service.WsChatlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 聊天记录表
 */
@Service
public class WsChatlogServiceImpl extends CoreServiceImpl<WsChatlogDao, WsChatlogDO> implements WsChatlogService {

    private static Logger logger = LoggerFactory.getLogger(WsChatlogServiceImpl.class);
}

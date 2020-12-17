package com.pjb.springbootjwt.zhddkk.service.impl;

import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsScheduledCronDao;
import com.pjb.springbootjwt.zhddkk.domain.WsScheduledCronDO;
import com.pjb.springbootjwt.zhddkk.service.WsScheduledCronService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时任务表.
 */
@Service
public class WsScheduledCronServiceImpl extends CoreServiceImpl<WsScheduledCronDao, WsScheduledCronDO> implements WsScheduledCronService {

    private static final Logger logger = LoggerFactory.getLogger(WsScheduledCronServiceImpl.class);
}

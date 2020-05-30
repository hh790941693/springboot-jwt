package com.pjb.springbootjwt.zhddkk.service.impl;

import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsFeedbackDao;
import com.pjb.springbootjwt.zhddkk.domain.WsFeedbackDO;
import com.pjb.springbootjwt.zhddkk.service.WsFeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 问题反馈
 */
@Service
public class WsFeedbackServiceImpl extends CoreServiceImpl<WsFeedbackDao, WsFeedbackDO> implements WsFeedbackService {

    private static Logger logger = LoggerFactory.getLogger(WsFeedbackServiceImpl.class);
}

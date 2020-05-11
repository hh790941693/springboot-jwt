package com.pjb.springbootjwt.zhddkk.service.impl;

import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsCircleCommentDao;
import com.pjb.springbootjwt.zhddkk.domain.WsCircleCommentDO;
import com.pjb.springbootjwt.zhddkk.service.WsCircleCommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
@Service
public class WsCircleCommentServiceImpl extends CoreServiceImpl<WsCircleCommentDao, WsCircleCommentDO> implements WsCircleCommentService {

    private static Logger logger = LoggerFactory.getLogger(WsCircleCommentServiceImpl.class);
}

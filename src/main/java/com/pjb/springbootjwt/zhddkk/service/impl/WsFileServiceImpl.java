package com.pjb.springbootjwt.zhddkk.service.impl;

import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsFileDao;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
import com.pjb.springbootjwt.zhddkk.service.WsFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件表
 */
@Service
public class WsFileServiceImpl extends CoreServiceImpl<WsFileDao, WsFileDO> implements WsFileService {

    private static Logger logger = LoggerFactory.getLogger(WsFileServiceImpl.class);
}

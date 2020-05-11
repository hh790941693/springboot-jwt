package com.pjb.springbootjwt.zhddkk.service.impl;

import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsDicDao;
import com.pjb.springbootjwt.zhddkk.domain.WsDicDO;
import com.pjb.springbootjwt.zhddkk.service.WsDicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字典表
 */
@Service
public class WsDicServiceImpl extends CoreServiceImpl<WsDicDao, WsDicDO> implements WsDicService {

    private static Logger logger = LoggerFactory.getLogger(WsDicServiceImpl.class);
}

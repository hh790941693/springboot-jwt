package com.pjb.springbootjwt.zhddkk.service.impl;

import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import com.pjb.springbootjwt.zhddkk.dao.WsOperationDao;
import com.pjb.springbootjwt.zhddkk.domain.WsOperationLogDO;
import com.pjb.springbootjwt.zhddkk.service.WsOperLogService;
import org.springframework.stereotype.Service;

@Service
public class WsOperLogServiceImpl extends CoreServiceImpl<WsOperationDao, WsOperationLogDO> implements WsOperLogService {
}

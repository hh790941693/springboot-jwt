package com.pjb.springbootjwt.zhddkk.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.pjb.springbootjwt.zhddkk.dao.WsOperationLogDao;
import com.pjb.springbootjwt.zhddkk.domain.WsOperationLogDO;
import com.pjb.springbootjwt.zhddkk.service.WsOperationLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志,主要记录增删改的日志 服务实现类
 * </p>
 *
 * @author huangchaohui
 * @since 2019-02-22
 */
@Service
public class WsOperationLogServiceImpl extends ServiceImpl<WsOperationLogDao, WsOperationLogDO> implements WsOperationLogService {

}

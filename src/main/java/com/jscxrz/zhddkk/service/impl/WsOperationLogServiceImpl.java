package com.jscxrz.zhddkk.service.impl;

import com.jscxrz.zhddkk.entity.WsOperationLogExt;
import com.jscxrz.zhddkk.dao.WsOperationLogDao;
import com.jscxrz.zhddkk.service.WsOperationLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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
public class WsOperationLogServiceImpl extends ServiceImpl<WsOperationLogDao, WsOperationLogExt> implements WsOperationLogService {

}

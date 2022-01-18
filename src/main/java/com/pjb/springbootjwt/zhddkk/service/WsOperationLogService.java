package com.pjb.springbootjwt.zhddkk.service;


import com.baomidou.mybatisplus.service.IService;
import com.pjb.springbootjwt.zhddkk.domain.WsOperationLogDO;
import com.pjb.springbootjwt.zhddkk.dto.LoginHistoryDto;

import java.util.List;

/**
 * 操作日志,主要记录增删改的日志 服务类.
 */
public interface WsOperationLogService extends IService<WsOperationLogDO> {
    List<LoginHistoryDto> queryOnlineUserData();
}

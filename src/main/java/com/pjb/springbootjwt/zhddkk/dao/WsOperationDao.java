package com.pjb.springbootjwt.zhddkk.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.pjb.springbootjwt.zhddkk.domain.WsOperationLogDO;
import org.springframework.stereotype.Repository;

/**
 * 操作日志.
 */
@Repository
public interface WsOperationDao extends BaseMapper<WsOperationLogDO> {

}

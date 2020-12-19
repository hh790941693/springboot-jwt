package com.pjb.springbootjwt.zhddkk.service;

import com.pjb.springbootjwt.zhddkk.domain.WsCommonDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUserProfileDO;

import java.util.List;

/**
 * 缓存
 */
public interface CacheService {
    void initCache();

    List<WsCommonDO> queryWsCommonList();

    List<WsUserProfileDO> queryWsUserProfileList();
}

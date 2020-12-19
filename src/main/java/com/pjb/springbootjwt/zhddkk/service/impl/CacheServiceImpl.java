package com.pjb.springbootjwt.zhddkk.service.impl;

import com.pjb.springbootjwt.zhddkk.domain.WsCommonDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUserProfileDO;
import com.pjb.springbootjwt.zhddkk.service.CacheService;
import com.pjb.springbootjwt.zhddkk.service.WsCommonService;
import com.pjb.springbootjwt.zhddkk.service.WsUserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheServiceImpl implements CacheService{

    private static Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

    @Autowired
    private WsCommonService wsCommonService;

    @Autowired
    private WsUserProfileService wsUserProfileService;

    private static final String COMMON = "common";

    private static final String USER_PROFILE = "userProfile";

    private static final Map<String, List<WsCommonDO>> commonMap = new ConcurrentHashMap<>();

    private static final Map<String, List<WsUserProfileDO>> userProfileMap = new ConcurrentHashMap<>();

    @Override
    public  void initCache() {
        logger.info("--------------------开始缓存数据-------------------");
        initCommon();
        initUserProfile();
    }

    @Override
    public List<WsCommonDO> queryWsCommonList(){
        return commonMap.get(COMMON);
    }

    @Override
    public List<WsUserProfileDO> queryWsUserProfileList(){
        return userProfileMap.get(USER_PROFILE);
    }

    private void initCommon(){
        List<WsCommonDO> list = wsCommonService.selectList(null);
        commonMap.put(COMMON, list);
    }

    private void initUserProfile(){
        List<WsUserProfileDO> list = wsUserProfileService.selectList(null);
        userProfileMap.put(USER_PROFILE, list);
    }
}

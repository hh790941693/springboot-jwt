package com.pjb.springbootjwt.zhddkk.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.zhddkk.cache.CoreCache;
import com.pjb.springbootjwt.zhddkk.domain.*;
import com.pjb.springbootjwt.zhddkk.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CacheServiceImpl implements CacheService {

    private static Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

    @Autowired
    private WsCommonService wsCommonService;

    @Autowired
    private WsUsersService wsUsersService;

    @Autowired
    private WsUserProfileService wsUserProfileService;

    @Autowired
    private WsFileService wsFileService;

    @Autowired
    private WsUserSessionService wsUserSessionService;

    @Override
    public void cacheAllData() {
        logger.info("--------------------开始缓存所有数据-------------------");
        initCommonData();
        initUserData();
        initUserProfileData();
        initUserFileData();
        initUserSessionData();
    }

    @Override
    public void cacheCommonData() {
        initCommonData();
    }

    @Override
    public void cacheUserData() {
        initUserData();
    }

    @Override
    public void cacheUserProfileData() {
        initUserProfileData();
    }

    @Override
    public void cacheUserFileData() {
        initUserFileData();
    }

    @Override
    public void cacheUserSessionData() {
        initUserSessionData();
    }

    private void initCommonData() {
        logger.info("缓存common数据");
        List<WsCommonDO> list = wsCommonService.selectList(null);
        CoreCache.getInstance().setCommonList(list);
    }

    private void initUserData() {
        logger.info("缓存用户数据");
        List<WsUsersDO> list = wsUsersService.selectList(null);
        CoreCache.getInstance().setUserList(list);
    }

    private void initUserProfileData() {
        logger.info("缓存用户门面数据");
        List<WsUserProfileDO> list = wsUserProfileService.selectList(null);
        CoreCache.getInstance().setUserProfileList(list);
    }

    private void initUserFileData() {
        logger.info("缓存用户文件数据");
        List<WsFileDO> list = wsFileService.selectList(new EntityWrapper<WsFileDO>().isNotNull("user"));
        CoreCache.getInstance().setUserFileList(list);
    }

    private void initUserSessionData() {
        logger.info("缓存用户会话数据");
        List<WsUserSessionDO> list = wsUserSessionService.selectList(null);
        CoreCache.getInstance().setUserSessionList(list);
    }
}

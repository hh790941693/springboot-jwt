package com.pjb.springbootjwt.zhddkk.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.zhddkk.cache.CoreCache;
import com.pjb.springbootjwt.zhddkk.domain.WsCommonDO;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUserProfileDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
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

    @Override
    public void cacheData() {
        logger.info("--------------------开始缓存数据-------------------");
        initCommonData();
        initUserData();
        initUserProfileData();
        initUserFileData();
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
}

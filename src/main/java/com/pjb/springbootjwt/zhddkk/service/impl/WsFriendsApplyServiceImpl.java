package com.pjb.springbootjwt.zhddkk.service.impl;

import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsFriendsApplyDao;
import com.pjb.springbootjwt.zhddkk.domain.WsFriendsApplyDO;
import com.pjb.springbootjwt.zhddkk.service.WsFriendsApplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 好友申请表
 */
@Service
public class WsFriendsApplyServiceImpl extends CoreServiceImpl<WsFriendsApplyDao, WsFriendsApplyDO> implements WsFriendsApplyService {

    private static Logger logger = LoggerFactory.getLogger(WsFriendsApplyServiceImpl.class);
}

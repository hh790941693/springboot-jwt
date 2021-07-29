package com.pjb.springbootjwt.zhddkk.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.pjb.springbootjwt.zhddkk.dto.WsChatroomDTO;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsChatroomDao;
import com.pjb.springbootjwt.zhddkk.domain.WsChatroomDO;
import com.pjb.springbootjwt.zhddkk.service.WsChatroomService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 聊天室房间表.
 * 作者:admin
 * 邮箱:anonymous@anonymous.com
 * 创建时间:2021-07-27 09:42:44
 */
@Service
public class WsChatroomServiceImpl extends CoreServiceImpl<WsChatroomDao, WsChatroomDO> implements WsChatroomService {

    private static final Logger logger = LoggerFactory.getLogger(WsChatroomServiceImpl.class);

    @Override
    public List<WsChatroomDTO> queryChatRoomInfoList(Wrapper<WsChatroomDO> wrapper, String loginUserId) {
        return this.baseMapper.queryChatRoomInfoList(wrapper, loginUserId);
    }
}

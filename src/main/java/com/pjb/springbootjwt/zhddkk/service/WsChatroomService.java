package com.pjb.springbootjwt.zhddkk.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.pjb.springbootjwt.zhddkk.domain.WsChatroomDO;
import com.pjb.springbootjwt.zhddkk.base.CoreService;
import com.pjb.springbootjwt.zhddkk.dto.WsChatroomDTO;

import java.util.List;

/**
 * 聊天室房间表.
 * 作者:admin
 * 邮箱:anonymous@anonymous.com
 * 创建时间:2021-07-27 09:42:44
 */
public interface WsChatroomService extends CoreService<WsChatroomDO> {

    List<WsChatroomDTO> queryChatRoomInfoList(Wrapper<WsChatroomDO> wrapper, String loginUserId);
}

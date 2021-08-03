package com.pjb.springbootjwt.zhddkk.service;

import com.pjb.springbootjwt.zhddkk.domain.WsChatroomUsersDO;
import com.pjb.springbootjwt.zhddkk.base.CoreService;
import com.pjb.springbootjwt.zhddkk.dto.WsChatroomUsersDTO;

import java.util.List;

/**
 * 聊天室人员信息.
 * 作者:admin
 * 邮箱:anonymous@anonymous.com
 * 创建时间:2021-07-27 09:42:47
 */
public interface WsChatroomUsersService extends CoreService<WsChatroomUsersDO> {
    public List<WsChatroomUsersDTO> queryChatroomUserList(String roomId);
}

package com.pjb.springbootjwt.zhddkk.dao;

import com.pjb.springbootjwt.zhddkk.domain.WsChatroomUsersDO;
import com.pjb.springbootjwt.zhddkk.base.BaseDao;
import com.pjb.springbootjwt.zhddkk.dto.WsChatroomUsersDTO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 聊天室人员信息.
 * 作者:admin
 * 邮箱:anonymous@anonymous.com
 * 创建时间:2021-07-27 09:42:47
 */
@Repository
public interface WsChatroomUsersDao extends BaseDao<WsChatroomUsersDO> {
    List<WsChatroomUsersDTO> queryChatroomUserList(@Param("roomId")String roomId);
}

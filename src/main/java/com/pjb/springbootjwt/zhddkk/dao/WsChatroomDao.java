package com.pjb.springbootjwt.zhddkk.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.pjb.springbootjwt.zhddkk.domain.WsChatroomDO;
import com.pjb.springbootjwt.zhddkk.base.BaseDao;
import com.pjb.springbootjwt.zhddkk.dto.WsChatroomDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 聊天室房间表.
 * 作者:admin
 * 邮箱:anonymous@anonymous.com
 * 创建时间:2021-07-27 09:42:44
 */
@Repository
public interface WsChatroomDao extends BaseDao<WsChatroomDO> {

    List<WsChatroomDTO> queryChatRoomInfoList(@Param("ew") Wrapper<WsChatroomDO> wrapper, @Param("loginUserId") String loginUserId);
}

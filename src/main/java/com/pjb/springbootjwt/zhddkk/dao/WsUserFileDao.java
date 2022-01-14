package com.pjb.springbootjwt.zhddkk.dao;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.pjb.springbootjwt.zhddkk.domain.WsUserFileDO;
import com.pjb.springbootjwt.zhddkk.base.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户文件表.
 * 作者:admin
 * 邮箱:anonymous@anonymous.com
 * 创建时间:2022-01-14 10:02:48
 */
@Repository
public interface WsUserFileDao extends BaseDao<WsUserFileDO> {
    List<WsUserFileDO> selectUserFileList(RowBounds rowBounds, @Param("ew") Wrapper<WsUserFileDO> wrapper);
}

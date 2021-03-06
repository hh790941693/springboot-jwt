package com.pjb.springbootjwt.zhddkk.dao;

import com.pjb.springbootjwt.zhddkk.base.BaseDao;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 文件表.
 */
@Repository
public interface WsFileDao extends BaseDao<WsFileDO> {
    //查询每个用户累计上传文件容量信息
    List<LinkedHashMap<String, String>> queryEachUserUploadFileSizeData();

    // 查询指定用户当天上传文件的容量
    @Select("SELECT IFNULL(SUM(t.file_size), 0) from ws_file t WHERE t.user_id=#{userId} AND TO_DAYS(t.create_time) = to_days(now());")
    Long queryUserTodayFileSize(@Param("userId")Long userId);
}

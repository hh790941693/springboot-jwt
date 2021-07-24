package com.pjb.springbootjwt.zhddkk.dao;

import com.pjb.springbootjwt.zhddkk.base.BaseDao;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
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
}

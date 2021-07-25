package com.pjb.springbootjwt.zhddkk.service;

import com.pjb.springbootjwt.zhddkk.base.CoreService;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 文件表.
 */
public interface WsFileService extends CoreService<WsFileDO> {
    //查询每个用户累计上传文件容量信息
    List<LinkedHashMap<String, String>> queryEachUserUploadFileSizeData();

    String queryUserTodayFileSize(String userName);
}

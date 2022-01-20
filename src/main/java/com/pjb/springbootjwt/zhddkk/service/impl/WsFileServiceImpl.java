package com.pjb.springbootjwt.zhddkk.service.impl;

import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsFileDao;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
import com.pjb.springbootjwt.zhddkk.service.WsFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 文件表
 */
@Service
public class WsFileServiceImpl extends CoreServiceImpl<WsFileDao, WsFileDO> implements WsFileService {

    private static Logger logger = LoggerFactory.getLogger(WsFileServiceImpl.class);

    @Override
    //查询每个用户累计上传文件容量信息
    public List<LinkedHashMap<String, String>> queryEachUserUploadFileSizeData() {
        return this.baseMapper.queryEachUserUploadFileSizeData();
    }

    @Override
    public Long queryUserTodayFileSize(Long userId) {
        return this.baseMapper.queryUserTodayFileSize(userId);
    }


}

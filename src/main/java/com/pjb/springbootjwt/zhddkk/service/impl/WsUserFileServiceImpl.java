package com.pjb.springbootjwt.zhddkk.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pjb.springbootjwt.zhddkk.dao.WsUserFileDao;
import com.pjb.springbootjwt.zhddkk.domain.WsUserFileDO;
import com.pjb.springbootjwt.zhddkk.service.WsUserFileService;
import com.pjb.springbootjwt.zhddkk.base.CoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 用户文件表.
 * 作者:admin
 * 邮箱:anonymous@anonymous.com
 * 创建时间:2022-01-14 10:02:48
 */
@Service
public class WsUserFileServiceImpl extends CoreServiceImpl<WsUserFileDao, WsUserFileDO> implements WsUserFileService {

    private static final Logger logger = LoggerFactory.getLogger(WsUserFileServiceImpl.class);

    @Autowired
    private WsUserFileDao wsUserFileDao;

    @Override
    public List<WsUserFileDO> selectUserFileList(Page<WsUserFileDO> page, Wrapper<WsUserFileDO> wrapper) {
        return wsUserFileDao.selectUserFileList(page, wrapper);
    }
}

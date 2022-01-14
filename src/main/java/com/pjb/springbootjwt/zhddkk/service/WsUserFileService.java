package com.pjb.springbootjwt.zhddkk.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.pjb.springbootjwt.zhddkk.domain.WsUserFileDO;
import com.pjb.springbootjwt.zhddkk.base.CoreService;

import java.util.List;

/**
 * 用户文件表.
 * 作者:admin
 * 邮箱:anonymous@anonymous.com
 * 创建时间:2022-01-14 10:02:48
 */
public interface WsUserFileService extends CoreService<WsUserFileDO> {

    List<WsUserFileDO> selectUserFileList(Page<WsUserFileDO> page, Wrapper<WsUserFileDO> wrapper);
}

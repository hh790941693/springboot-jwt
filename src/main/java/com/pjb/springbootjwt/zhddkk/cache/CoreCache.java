package com.pjb.springbootjwt.zhddkk.cache;

import java.util.ArrayList;
import java.util.List;
import com.pjb.springbootjwt.zhddkk.domain.WsCommonDO;
import com.pjb.springbootjwt.zhddkk.domain.WsFileDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUserProfileDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;

public class CoreCache {
    private static CoreCache coreCache = null;

    private List<WsCommonDO> commonList = null;

    private List<WsUsersDO> userList = null;

    private List<WsUserProfileDO> userProfileList = null;

    private List<WsFileDO> userFileList = null;

    public static CoreCache getInstance() {
        if (null == coreCache) {
            coreCache = new CoreCache();
        }
        return coreCache;
    }

    public List<WsCommonDO> getCommonList() {
        if (null == commonList) {
            commonList = new ArrayList<>();
        }
        return commonList;
    }

    public void setCommonList(List<WsCommonDO> commonList) {
        this.commonList = commonList;
    }

    public List<WsUsersDO> getUserList() {
        if (null == userList) {
            userList = new ArrayList<>();
        }
        return userList;
    }

    public void setUserList(List<WsUsersDO> userList) {
        this.userList = userList;
    }

    public List<WsUserProfileDO> getUserProfileList() {
        if (null == userProfileList) {
            userProfileList = new ArrayList<>();
        }
        return userProfileList;
    }

    public void setUserProfileList(List<WsUserProfileDO> userProfileList) {
        this.userProfileList = userProfileList;
    }

    public List<WsFileDO> getUserFileList() {
        if (null == userFileList) {
            userList = new ArrayList<>();
        }
        return userFileList;
    }

    public void setUserFileList(List<WsFileDO> userFileList) {
        this.userFileList = userFileList;
    }
}

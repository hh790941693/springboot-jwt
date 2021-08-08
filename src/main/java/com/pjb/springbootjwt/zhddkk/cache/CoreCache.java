package com.pjb.springbootjwt.zhddkk.cache;

import java.util.ArrayList;
import java.util.List;

import com.pjb.springbootjwt.zhddkk.domain.*;

public class CoreCache {
    private static CoreCache coreCache = null;

    private List<WsCommonDO> commonList = null;

    private List<WsUsersDO> userList = null;

    private List<WsUserProfileDO> userProfileList = null;

    private List<WsFileDO> userFileList = null;

    private List<WsUserSessionDO> userSessionList = null;

    public static CoreCache getInstance() {
        synchronized (CoreCache.class) {
            if (null == coreCache) {
                coreCache = new CoreCache();
            }
            return coreCache;
        }
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

    public WsUserProfileDO getUserProfile(String username) {
        if (null == userProfileList) {
            return null;
        }

        WsUserProfileDO wsUserProfileDO = null;
        for (WsUserProfileDO wupd : userProfileList) {
            if (wupd.getUserName().equals(username)) {
                wsUserProfileDO = wupd;
                break;
            }
        }
        return wsUserProfileDO;
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
        if (null == userFileList) {
            userList = new ArrayList<>();
        }
        this.userFileList = userFileList;
    }

    public List<WsUserSessionDO> getUserSessionList() {
        if (null == userSessionList) {
            userSessionList = new ArrayList<>();
        }
        return userSessionList;
    }

    public void setUserSessionList(List<WsUserSessionDO> userSessionList) {
        this.userSessionList = userSessionList;
    }
}

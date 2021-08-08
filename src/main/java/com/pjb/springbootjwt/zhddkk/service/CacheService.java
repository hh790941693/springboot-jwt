package com.pjb.springbootjwt.zhddkk.service;

/**
 * 缓存.
 */
public interface CacheService {
    void cacheAllData();

    void cacheCommonData();

    void cacheUserData();

    void cacheUserProfileData();

    void cacheUserFileData();

    void cacheUserSessionData();
}

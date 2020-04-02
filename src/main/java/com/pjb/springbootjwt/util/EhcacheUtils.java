package com.pjb.springbootjwt.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EhcacheUtils {

    private static Logger logger = LoggerFactory.getLogger(EhcacheUtils.class);

    private static final String path = "/ehcache.xml";
    public static final String default_cache_name = "users";

    private URL url;
    private static CacheManager manager;
    private static EhcacheUtils ehCache;

    private EhcacheUtils(String path) {
        url = getClass().getResource(path);
        manager = CacheManager.create(url);
    }

    public static EhcacheUtils getInstance() {
        if (ehCache == null) {
            logger.info("........初始化ehcache.........");
            ehCache = new EhcacheUtils(path);
        }
        return ehCache;
    }
    static {
        getInstance();
    }
    public static void put(String cacheName, Object key, Object value) {
        Cache cache = manager.getCache(cacheName);
        Element element = new Element(key, value);
        cache.put(element);
        cache.flush();
    }

    public static Object get(String cacheName, Object key) {
        Cache cache = manager.getCache(cacheName);
        Element element = cache.get(key);
        cache.flush();
        return element == null ? null : element.getObjectValue();
    }

    public static Cache get(String cacheName) {
        return manager.getCache(cacheName);
    }

    public static List<String> getKeyList(String cacheName){
        List<String> list = new ArrayList<>();
        Cache cache = manager.getCache(cacheName);
        if (null != cache){
            list = cache.getKeys();
        }

        return list;
    }

    public static Map<String, Object> getAllCache(String cacheName){
        Map<String, Object> map = new HashMap<>();
        List<String> keyList = getKeyList(cacheName);
        for (String k : keyList){
            Object v = get(cacheName, k);
            if (!map.containsKey(k)){
                map.put(k, v);
            }
        }

        return map;
    }

    public static void remove(String cacheName, Object key) {
        Cache cache = manager.getCache(cacheName);
        cache.remove(key);
    }
}
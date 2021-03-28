package com.pjb.springbootjwt.zhddkk.interceptor;

import com.pjb.springbootjwt.zhddkk.util.UnicodeUtil;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * 初始化数据用.
 */
@Configuration
public class WsInterceptor implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(WsInterceptor.class);

    // config.properties中的记录
    public static Map<String, String> configMap = new HashMap<String, String>();

    // chatmapping.properties中的记录
    public static Map<String, String> chatMappingMap = new HashMap<>();

    public static Map<String, String> getConfigMap() {
        return configMap;
    }

    public static void setConfigMap(Map<String, String> configMap) {
        WsInterceptor.configMap = configMap;
    }

    public static Map<String, String> getChatMappingMap() {
        return chatMappingMap;
    }

    public static void setChatMappingMap(Map<String, String> chatMappingMap) {
        WsInterceptor.chatMappingMap = chatMappingMap;
    }

    @Override
    public void afterPropertiesSet() {
        logger.info("WsInterceptor call afterPropertiesSet()");
        loadData();
    }

    private void loadData() {
        configMap.clear();
        chatMappingMap.clear();

        // 加载配置文件config.properties
        try {
            loadConfigPropertiesData("config.properties", configMap);
        } catch (Exception e) {
            loadDefaultConfigData(configMap);
            System.out.println("初始化config.properties失败!" + e.getMessage());
            logger.error("初始化config.properties失败!", e);
        }

        // 加载配置文件chatmapping.properties
        try {
            loadConfigPropertiesData("chatmapping.properties", chatMappingMap);
        } catch (Exception e) {
            System.out.println("初始化chatmapping.properties失败!" + e.getMessage());
            logger.error("初始化chatmapping.properties失败!", e);
        }
    }

    /**
     * 加载指定的配置文件.
     *
     * @param filename 配置文件名 路径位于resources\下
     * @param map 字典对象
     * @throws Exception 异常
     */
    private void loadConfigPropertiesData(String filename, Map<String, String> map) throws Exception {
        if (StringUtils.isBlank(filename)) {
            return;
        }
        logger.info("load data from:" + filename);

        InputStream stream = null;
        InputStreamReader reader = null;
        BufferedReader br = null;
        try {
            stream = getClass().getClassLoader().getResourceAsStream(filename);
            reader = new InputStreamReader(stream, "UTF-8");
            br = new BufferedReader(reader);
            String data;
            while ((data = br.readLine()) != null) {
                if (data.startsWith("#")) {
                    continue;
                }
                if (data.trim().equals("")) {
                    continue;
                }
                String key = data.split("=")[0].trim();
                key = UnicodeUtil.unicode2String(key);
                String value = "";
                if (data.split("=").length > 1) {
                    value = data.split("=")[1].trim();
                    value = UnicodeUtil.unicode2String(value);
                }
                if (!map.containsKey(key)) {
                    map.put(key, value);
                }
            }
            logger.info("load data from {} complete!", filename);
        } catch (Exception e) {
            logger.info("查找文件" + filename + "失败! 改从ServiceConstants中加载配置");
            System.out.println("查找文件" + filename + "失败! 改从ServiceConstants中加载配置");
            throw new Exception(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    /**
     * 加载默认配置信息.
     * @param map 字典对象
     */
    private void loadDefaultConfigData(Map<String, String> map) {
        // 版本号
        logger.info("加载默认配置数据");
        map.put("author", "huangchaohui");
        map.put("contact", "547495788@qq.com");
        map.put("address", "Flower Gaden#5,Freedom street,Da Ye,Huang Shi,Wu Han,Hu Bei province");
        map.put("copyRight", "All Rights reserved@2018-2035");
        map.put("version", "1.0-Snapshot");
    }
}
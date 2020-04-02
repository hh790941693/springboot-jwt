package com.pjb.springbootjwt.ump.controller;

import com.pjb.springbootjwt.common.redis.RedisUtil;
import com.pjb.springbootjwt.common.vo.Result;
import com.pjb.springbootjwt.ump.bean.TokenBean;
import com.pjb.springbootjwt.ump.constant.Constant;
import com.pjb.springbootjwt.util.EhcacheUtils;
import com.pjb.springbootjwt.util.JSONUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 游客可以随意访问的接口
 */
@RestController
@RequestMapping("/anon")
@Api(tags = "游客访问接口")
public class AnonController {

    private static Logger logger = LoggerFactory.getLogger(AnonController.class);

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取ehcache缓存数据
     * @return
     */
    @GetMapping("/queryEhcache")
    @ApiOperation("获取ehCache缓存数据")
    public Result<String> queryEhcache(){
        Map<String, Object> map = EhcacheUtils.getAllCache(EhcacheUtils.default_cache_name);
        String res = JSONUtils.beanToJson(map);
        logger.info("缓存中所有的数据:{}", map);

        String tmpdir = System.getProperty("java.io.tmpdir");
        ObjectInputStream ois = null;
        FileInputStream fis = null;
        try{
            File f = new File(tmpdir+File.separator+"users.data");
            if (f.exists() && f.isFile()) {
                fis = new FileInputStream(f);
                ois = new ObjectInputStream(fis);
                Element element = (Element) ois.readObject();
                Object key = element.getObjectKey();
                Object value = element.getObjectValue();
                logger.info("key:{}", key);
                logger.info("value:{}", value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (null != fis){
                try {
                    fis.close();
                }catch (Exception e){
                    logger.info("关闭fis失败:{}",e.getMessage());
                }
            }
            if (null != ois){
                try {
                    ois.close();
                }catch (Exception e){
                    logger.info("关闭ois失败:{}",e.getMessage());
                }
            }
        }
        return Result.ok(res);
    }

    /**
     * 获取redis缓存数据
     * @return
     */
    @GetMapping("/queryRedis")
    @ApiOperation("获取redis缓存数据")
    public Result<List<TokenBean>> queryRedis(){
        Set<String> keySet = redisUtil.allKeys(Constant.TOKEN_PREFIX +"*");

        logger.info("redis中所有的key:{}", keySet);
        List<TokenBean> list = new ArrayList<>();
        for (String key : keySet){
            Object object = redisUtil.get(key);
            TokenBean tokenBean = (TokenBean)object;

            list.add(tokenBean);
        }

        return Result.ok(list);
    }
}

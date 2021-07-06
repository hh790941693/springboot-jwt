package com.pjb.springbootjwt.shop;

import org.apache.commons.lang.StringUtils;

import java.util.Random;
import java.util.UUID;

/**
 * 超市工具类
 */
public class ShopUtil {

    /**
     * 生成对象ID.
     * 比如：商品id、店铺id等.
     * @param preFix 前缀
     * @return
     */
    public static String buildObjectId(String preFix) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(preFix)) {
            sb.append(preFix);
        }
        sb.append(UUID.randomUUID().toString().replaceAll("-", ""));
        return sb.toString();
    }

    /**
     * 生成订单号
     * @param preFix 前缀
     * @param userId 用户id
     * @return
     */
    public static String buildOrderNo(String preFix, String userId) {
        StringBuilder sb = new StringBuilder();
        //前缀
        if (StringUtils.isNotBlank(preFix)) {
            sb.append(preFix);
        }
        //用户名
        if (StringUtils.isNotBlank(userId)) {
            sb.append(userId);
        }
        //4个随机数
        Random random = new Random();
        for (int i=0;i<=3;i++) {
            sb.append(random.nextInt(10));
        }
        sb.append("-");

        // 当前时间戳后4位
        long timestamp = System.currentTimeMillis();
        String timestampStr = String.valueOf(timestamp).substring(9);
        sb.append(timestampStr).append("-");

        //UUID的hash值
        int hashCode = UUID.randomUUID().toString().hashCode();
        if(hashCode < 0) {
            hashCode = - hashCode;
        }
        sb.append(hashCode);
        return sb.toString();
    }
}

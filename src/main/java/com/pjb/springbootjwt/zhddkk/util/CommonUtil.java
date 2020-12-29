package com.pjb.springbootjwt.zhddkk.util;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

public class CommonUtil {
    /**
     * 获取xml中某个节点中的值.
     *
     * @param xmlStr 字符串
     * @param name 节点名
     * @return 结果
     */
    public static String getVarFromXml(String xmlStr, String name) {
        String result = null;

        if (xmlStr.contains(name)) {
            String leftStr = "<" + name + ">";
            String rightStr = "</" + name + ">";
            result = xmlStr.split(leftStr)[1].split(rightStr)[0].trim();
        }

        return result;
    }

    /**
     * 判断一个字符串是否为空.
     * true:空  false:非空
     *
     * @param str 字符串
     * @return boolean 结果
     */
    public static boolean validateEmpty(String str) {
        return StringUtils.isBlank(str);
    }

    /**
     * 转义xml中的特殊字符.
     *
     * @param str 字符串
     * @return 结果
     */
    public static String changeStrToXml(String str) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("&lt;", "<");
        map.put("&gt;", ">");
        map.put("&amp;", "&");

        for (Entry<String, String> entry : map.entrySet()) {
            str = str.replaceAll(entry.getKey(), entry.getValue());
        }

        if (str.contains("&amp;")) {
            str = str.replaceAll("&amp;", map.get("&amp;"));
        }

        return str;
    }

    /**
     * 获取前几个小时时间.
     *
     * @param curDate 当前时间
     * @param hour   前几个小时
     *
     * @return 结果
     */
    public static Date queryPreHour(Date curDate, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(curDate);
        c.add(Calendar.HOUR, -hour);
        return c.getTime();
    }

    /**
     * 获取前几分钟时间.
     *
     * @param curDate 当前时间
     * @param minutes 前几分钟
     * @return 结果
     */
    public static Date queryPreMinutes(Date curDate, int minutes) {
        Calendar c = Calendar.getInstance();
        c.setTime(curDate);
        c.add(Calendar.MINUTE, -minutes);
        return c.getTime();
    }

    /**
     * 获取2个时间的间隔  单位:分钟.
     * 注意:date1比date2大
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 结果
     */
    public static long getTwoDateInterval(Date date1, Date date2) {
        long misec = date1.getTime() - date2.getTime();
        return misec / 1000 / 60;
    }

    /**
     * 随机获取两个整型数字之间的数.
     * 起始数字要大于结束数字
     *
     * @param start 起始数字
     * @param end  结束数字
     * @return 结果
     */
    public static int randomNumber(int start, int end) {
        int result = 0;
        if (end > start) {
            int interval = end - start;
            Random rand = new Random();

            result = rand.nextInt(interval) + start;
        }

        return result;
    }

    /**
     * 随机获取两个double数字之间的数.
     * 起始数字要大于结束数字
     *
     * @param d1 起始数字
     * @param d2  结束数字
     * @return 结果
     */
    public static double randomDouble(double d1, double d2) {
        double result = 0d;
        if (d2 > d1) {
            result = Math.random() * (d2 - d1) + d1;
        }

        return result;
    }

    /**
     * 让double型保留几位小数.
     *
     * @param d double数据
     * @param scale 保留位数
     * @return 结果
     */
    public static double changeDouble(double d, int scale) {
        double result;
        BigDecimal bd = new BigDecimal(d);
        result = bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();

        return result;
    }

    /**
     * 比较2个string是否一样.
     * true:一样
     * false:不一样
     *
     * @param s1 字符串1
     * @param s2 字符串2
     */
    public static boolean compareToObj(String s1, String s2) {
        boolean res = false;
        if (s1 == null && s2 == null) {
            res = true;
        } else if (s1 != null && s2 != null) {
            if (s1.equals(s2)) {
                res = true;
            }
        }
        return res;
    }

    /**
     * 检查url是否可访问.
     */
    public static boolean testUrl(String urlString) {
        InputStream inputStream = null;
        try {
            URL url = new URL(urlString);
            inputStream = url.openStream();
            return true;
        } catch (Exception e1) {
            // do nothing
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 分页.
     * @param list 列表
     * @param current 当前页数(从0开始)
     * @param size 每页条数
     * @param <T> dto
     * @return
     */
    public static <T> List<T> pageToList(List<T> list, int current, int size){
        int start = 0;
        int end = 0;
        if (current == 1) {
            start = 0;
        } else {
            start = (current - 1) * size;
        }
        end = start + size-1;
        if (end > list.size()){
            end = list.size();
        }
        return list.subList(start, end);
    }
}

package com.pjb.springbootjwt.zhddkk.util;

import net.sf.json.JSONObject;

/**
 * java对象和json字符串互转.
 * 
 * @author Administrator
 *
 */
public class JsonUtil {
    /**
     * java bean对象转换成json字符串.
     *
     * @param o
     * @return
     */
    public static String javaobject2Jsonstr(Object o) {
        JSONObject jsonObject = JSONObject.fromObject(o);
        return jsonObject.toString();
    }
    
    /**
     * json字符串转换为java bean对象.
     *
     * @param json
     * @param classT
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonstr2Javaobject(String json, Class<T> classT) {
        JSONObject jsonObject = JSONObject.fromObject(json);
        if (classT instanceof Object) {
            System.out.println("xxxx");
        }
        return (T)JSONObject.toBean(jsonObject, classT);
    }
    
    /**
     * java bean对象转换成json对象.
     *
     * @param o
     * @return
     */
    public static JSONObject javaobject2Jsonobject(Object o) {
        return JSONObject.fromObject(o);
    }
    
    /**
     * json字符串转换成json对象.
     *
     * @param str
     * @return
     */
    public static JSONObject jsonstr2Jsonobject(String str) {
        return JSONObject.fromObject(str);
    }
}

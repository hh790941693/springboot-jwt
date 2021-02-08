package com.pjb.springbootjwt.zhddkk.util;

import java.util.UUID;
import java.util.Collection;
import java.util.Map;

public class StringUtil {

    /**
     * 判定对象是否为空.
     *
     * @param o 对象
     * @return 空的场合：true／非空：false
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if ((o instanceof String)) {
            if (((String) o).trim().length() == 0) {
                return true;
            }
        } else if ((o instanceof Collection)) {
            if (((Collection) o).isEmpty()) {
                return true;
            }
        } else if (o.getClass().isArray()) {
            if (((Object[]) o).length == 0) {
                return true;
            }
        } else if ((o instanceof Map)) {
            if (((Map) o).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判定对象是否不为空.
     *
     * @param o 对象
     * @return 非空的场合：true／空：false
     */
    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);

    }

    /**
     * 右側の指定した文字列を削ります.
     *
     * @param text テキスト
     * @param trimText 削る文字列
     * @return 結果の文字列
     */
    public static final String rtrim(final String text, String trimText) {
        if (text == null) {
            return null;
        }
        if (trimText == null) {
            trimText = " ";
        }
        int pos = text.length() - 1;
        for (; pos >= 0; pos--) {
            if (trimText.indexOf(text.charAt(pos)) < 0) {
                break;
            }
        }
        return text.substring(0, pos + 1);
    }

    /**
     * 左側の指定した文字列を削ります.
     *
     * @param text テキスト
     * @param trimText 削る文字列
     * @return 結果の文字列
     */
    public static final String ltrim(final String text, String trimText) {
        if (text == null) {
            return null;
        }
        if (trimText == null) {
            trimText = " ";
        }
        String temp = text;
        while (temp.indexOf(trimText) == 0) {
            temp = temp.replaceFirst(trimText, "");
        }
        return temp;
    }

    /**
     * 首字母大写.
     *
     * @param str 字符串
     * @return 首字母大写字符串
     */
    public static String firstChar2UpperCase(String str) {
        if (isNotEmpty(str)) {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return "";

    }

    /**
     * 首字母小写.
     *
     * @param str 字符串
     * @return 首字母小写字符串
     */
    public static String firstChar2LowerCase(String str) {
        if (isNotEmpty(str)) {
            return str.substring(0, 1).toLowerCase() + str.substring(1);
        }
        return "";

    }

    /**
     * uuid 取得.
     * @return uuid
     */
    public static String getUuid() {
        String uuid = UUID.randomUUID().toString(); //转化为String对象
        uuid = uuid.replace("-", ""); //因为UUID本身为32位只是生成时多了“-”，所以将它们去点就可
        return uuid;
    }
}

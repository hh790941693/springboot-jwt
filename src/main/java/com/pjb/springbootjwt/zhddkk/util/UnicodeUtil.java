package com.pjb.springbootjwt.zhddkk.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnicodeUtil {

    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        if (StringUtils.isNotBlank(string)) {
            for (int i = 0; i < string.length(); i++) {
                // 取出每一个字符
                char c = string.charAt(i);
                String toHexStr = Integer.toHexString(c);
                if (toHexStr.startsWith("d")) {
                    unicode.append("\\u" + toHexStr);
                } else {
                    unicode.append(c);
                }
            }
        }

        return unicode.toString();
    }

    /**
     * unicode转字符串
     * @param str
     * @return
     */
    public static String unicode2String(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }
}

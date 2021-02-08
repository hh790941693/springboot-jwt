import com.pjb.springbootjwt.zhddkk.util.StringUtil;

public class Ognl {
    /**
     * 判定对象是否为空.
     *
     * @param o 对象
     * @return 空的场合：true／非空：false
     */
    public static boolean isEmpty(Object o) {
        return StringUtil.isEmpty(o);
    }

    /**
     * 判定对象是否不为空.
     *
     * @param o 对象
     * @return 非空的场合：true／空：false
     */
    public static boolean isNotEmpty(Object o) {
        return (!(isEmpty(o)));
    }

    /**
     * 判定对象是否相等.
     *
     * @param o1 比较对象1
     * @param o2 比较对象2
     * @return 相等的场合：true／空：false
     */
    public static boolean isEqual(String o1, String o2) {
        if (isEmpty(o1)) {
            return false;
        }
        return o1.equals(o2);
    }

    /**
     * 判定对象是否不相等.
     *
     * @param o1 比较对象1
     * @param o2 比较对象2
     * @return 不相等的场合：true／空：false
     */
    public static boolean isNotEqual(String o1, String o2) {
        return !isEqual(o1, o2);
    }

    /**
     * 判定对象是否在一览中.
     *
     * @param o1 判定对象
     * @param o2 一览对象
     * @return 在的场合：true／空：false
     */
    public static boolean isInList(String o1, String o2) {
        if (!o2.endsWith(",")) {
            o2 = o2 + ",";
        }
        return o2.indexOf(o1 + ",") >= 0;
    }

    /**
     * 连接两个字符串.
     *
     * @param o1 对象1
     * @param o2 对象2
     * @return 连接后的字符串
     */
    public static String concat(String o1, String o2) {
        return o1.concat(o2);
    }
}
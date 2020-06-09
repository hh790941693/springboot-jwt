package com.pjb.springbootjwt.zhddkk.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    //获取前/后几个小时的时间
    public static Date getBeforeByHourTime(Date curDate, int ihour){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + ihour);
        return calendar.getTime();
    }

    //获取前/后几天的时间
    public static Date getBeforeByDayTime(Date curDate, int iday){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.add(Calendar.DAY_OF_MONTH, iday);
        return calendar.getTime();
    }

    //获取前/后几月的时间
    public static Date getBeforeByMonthTime(Date curDate, int imonth){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, imonth);
        return calendar.getTime();
    }

    public static String getFirstDayOfMonthStr(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH,firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return sdf.format(cal.getTime());
    }

    public static String getLastDayOfMonthStr(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH,lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return sdf.format(cal.getTime());
    }

    public static Date getFirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH,firstDay);
        return cal.getTime();
    }

    public static Date getLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        cal.set(Calendar.HOUR,23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        //格式化日期
        return cal.getTime();
    }

    /**
     * 获取一天的起始时间
     * @param date
     * @return
     */
    public static Date dayBeginDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        return calendar.getTime();
    }

    /**
     * 获取一天的结束时间
     * @param date
     * @return
     */
    public static Date dayEndDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);

        return calendar.getTime();
    }
}

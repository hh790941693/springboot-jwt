package com.pjb.springbootjwt.zhddkk.util;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    
    /**
     * 获取一天的起始时间.
     * 
     * @param date
     * @return
     */
    public static Date getDayStart(Date date) {
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTime(date);
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        
        System.out.println(todayStart.getTime());
        return todayStart.getTime();
    }
    
    /**
     * 获取一天的结束时间.
     * 
     * @param date
     * @return
     */
    public static Date getDayEnd(Date date) {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTime(date);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        
        System.out.println(todayEnd.getTime());
        return todayEnd.getTime();
    }
}

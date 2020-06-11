package com.pjb.springbootjwt.zhddkk.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestJavaCode {

    public static void main(String[] args){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i=0;i<=6;i++) {
            Date date = DateUtil.getBeforeByDayTime(new Date(), -i);

            String dayBeginTimeStr = sdf.format(date)+" 00:00:00";
            String dayEndTimeStr = sdf.format(date)+" 23:59:59";
            System.out.println(date +"  "+dayBeginTimeStr+"   "+dayEndTimeStr);

            System.out.println(dayBeginDate(date));
            System.out.println(dayEndDate(date));
        }
    }

    public static Date dayBeginDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        return calendar.getTime();
    }

    public static Date dayEndDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);

        return calendar.getTime();
    }
}

package com.pjb.springbootjwt.zhddkk.quartz;

public class JobConstant {
    public static final String STATUS_RUNNING = "1";
    public static final String STATUS_NOT_RUNNING = "0";
    public static final String CONCURRENT_IS = "1";
    public static final String CONCURRENT_NOT = "0";

    // 停止计划任务
    public static String STATUS_RUNNING_STOP = "stop";
    // 开启计划任务
    public static String STATUS_RUNNING_START = "start";

    public static String DEFAULT_JOB_METHOD_NAME = "execute";
}

package com.pjb.springbootjwt.zhddkk.bean;

import lombok.Data;

@Data
public class WeatherBean {
    private String time;
    private String weekName;
    private String highTemperature;
    private String lowTemperature;
    private String weather;
    private String lastUpdateTime;
    private String location;
    private String remark;
}

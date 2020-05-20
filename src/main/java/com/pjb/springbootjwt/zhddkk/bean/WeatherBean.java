package com.pjb.springbootjwt.zhddkk.bean;

public class WeatherBean {
    private String time;
    private String weekName;
    private String highTemperature;
    private String lowTemperature;
    private String weather;
    private String lastUpdateTime;
    private String location;

    private String remark;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public String getHighTemperature() {
        return highTemperature;
    }

    public void setHighTemperature(String highTemperature) {
        this.highTemperature = highTemperature;
    }

    public String getLowTemperature() {
        return lowTemperature;
    }

    public void setLowTemperature(String lowTemperature) {
        this.lowTemperature = lowTemperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "WeatherBean{" +
                "time='" + time + '\'' +
                ", weekName='" + weekName + '\'' +
                ", highTemperature='" + highTemperature + '\'' +
                ", lowTemperature='" + lowTemperature + '\'' +
                ", weather='" + weather + '\'' +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                ", location='" + location + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}

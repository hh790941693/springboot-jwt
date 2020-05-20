package com.pjb.springbootjwt.zhddkk.util;

import com.pjb.springbootjwt.zhddkk.bean.WeatherBean;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherUtil {
    //江都天气
    private static String weatherUrl = "http://www.weather.com.cn/weather1d/101190605.shtml";

    public static WeatherBean grapWeatherInfo() throws Exception{
        String weatherInfo = "";
        String lastUpdateTime = "";
        String location = "";
        WeatherBean weatherBean = new WeatherBean();
        try {
            Connection connection = Jsoup.connect(weatherUrl).timeout(10000);
            Document doc = connection.get();
            //获取天气
            Elements todayElements = doc.getElementsByClass("today");
            for (Element todayEle : todayElements) {
                if (todayEle.hasAttr("id") && todayEle.attr("id").equals("today")) {
                    Element todayElement = todayEle.getElementById("today");
                    Elements inputElements = todayElement.getElementsByTag("input");
                    for (Element inputEle : inputElements) {
                        if (!inputEle.attr("type").equals("button") && inputEle.hasAttr("value")) {
                            if (inputEle.attr("id").equals("hidden_title")) {
                                //05月20日08时 周三  多云  30/16°C
                                weatherInfo = inputEle.attr("value");
                            }else if (inputEle.attr("id").equals("update_time")) {
                                lastUpdateTime = inputEle.attr("value");
                            }
                        }
                    }
                    break;
                }
            }

            //获取地区
            Elements placeElements = doc.getElementsByClass("crumbs");
            for (Element placeEle : placeElements) {
                if (placeEle.hasClass("fl")) {
                    Elements spanElements = placeEle.getElementsByTag("span");
                    for (Element spanEle : spanElements) {
                        if (!spanEle.html().contains(";")) {
                            location = spanEle.text();
                        }
                    }
                    break;
                }
            }

        } catch (Exception e) {
            throw new Exception(e);
        }

        if (StringUtils.isNotBlank(location)){
            weatherBean.setLocation(location);
        }
        if (StringUtils.isNotBlank(lastUpdateTime)){
            weatherBean.setLastUpdateTime(lastUpdateTime);
        }
        if (StringUtils.isNotBlank(weatherInfo)){
            //05月20日08时 周三  多云  30/16°C
            Pattern p = Pattern.compile("\\s+");
            Matcher m = p.matcher(weatherInfo);
            weatherInfo = m.replaceAll(" ");

            String[] wehtherArr = weatherInfo.split(" ");
            weatherBean.setTime(wehtherArr[0]);
            weatherBean.setWeekName(wehtherArr[1]);
            weatherBean.setWeather(wehtherArr[2]);
            String[] temperatureArr = wehtherArr[3].split("/");
            weatherBean.setHighTemperature(temperatureArr[0]);
            weatherBean.setLowTemperature(temperatureArr[1]);
            weatherBean.setRemark(weatherInfo);
        }
        System.out.println(weatherInfo);
        System.out.println(lastUpdateTime);
        System.out.println(location);

        return weatherBean;
    }
}

package com.pjb.springbootjwt.zhddkk.controller;

import com.pjb.springbootjwt.common.vo.Result;
import com.pjb.springbootjwt.zhddkk.bean.WeatherBean;
import com.pjb.springbootjwt.zhddkk.util.WeatherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/index")
public class WsIndexController {

    private static Logger logger = LoggerFactory.getLogger(WsIndexController.class);

    /**
     * 获取天气
     *
     * @return
     */
    @RequestMapping("/grapWetherInfo")
    @ResponseBody
    public Result<WeatherBean> grapWetherInfo(){
        try {
            WeatherBean weatherBean = WeatherUtil.grapWeatherInfo();
            return Result.ok(weatherBean);
        }catch (Exception e){

        }
        return Result.fail();
    }

    //联系作者
    @RequestMapping("/contactAuthor.page")
    public String contactAuthor(){
        return "ws/contactAuthor";
    }

    //捐赠作者
    @RequestMapping("/donate.page")
    public String donate(){
        return "ws/donate";
    }

    //提出疑问
    @RequestMapping("/feedbackUs.page")
    public String feedbackUs(){
        return "ws/feedbackUs";
    }

    //关于我们
    @RequestMapping("/aboutUs.page")
    public String aboutUs(){
        return "ws/aboutUs";
    }
}

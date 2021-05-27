package com.pjb.springbootjwt.zhddkk.controller;

import com.pjb.springbootjwt.common.vo.Result;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.bean.SessionInfoBean;
import com.pjb.springbootjwt.zhddkk.bean.WeatherBean;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.constants.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.constants.OperationEnum;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
import com.pjb.springbootjwt.zhddkk.util.WeatherUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 首页相关页面.
 */
@Controller
@RequestMapping("/index")
public class WsIndexController {

    private static final Logger logger = LoggerFactory.getLogger(WsIndexController.class);

    /**
     * 获取天气.
     *
     * @return 天气信息
     */
    @RequestMapping("/grapWetherInfo")
    @ResponseBody
    public Result<WeatherBean> grapWetherInfo() {
        logger.info("获取天气信息");
        try {
            WeatherBean weatherBean = WeatherUtil.grapWeatherInfo();
            return Result.ok(weatherBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail();
    }

    /**
     * session信息页面.
     *
     * @param model 模型
     * @return 会话信息
     */
    @RequestMapping("/sessionInfo.page")
    public String sessionInfoPage(Model model) {
        SessionInfoBean sessionInfoBean = SessionUtil.getSessionAttribute(CommonConstants.SESSION_INFO);

        if (null != sessionInfoBean) {
            SessionInfoBean copyBean = new SessionInfoBean();
            BeanUtils.copyProperties(sessionInfoBean, copyBean);
            String encryPassword = hidePassword(sessionInfoBean.getPassword());
            copyBean.setPassword(encryPassword);
            model.addAttribute(CommonConstants.SESSION_INFO, JsonUtil.javaobject2Jsonobject(copyBean));
            return "index/sessionInfo";
        }
        return "redirect:/";
    }

    /**
     * 联系作者.
     * @return 联系作者页面
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.OTHER, subModule = "", describe = "联系作者页面")
    @RequestMapping("/contactAuthor.page")
    public String contactAuthor() {
        return "index/contactAuthor";
    }

    /**
     * 捐赠作者.
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.OTHER, subModule = "", describe = "捐赠作者页面")
    @RequestMapping("/donate.page")
    public String donate() {
        return "index/donate";
    }

    /**
     * 提出疑问.
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.OTHER, subModule = "", describe = "提出疑问页面")
    @RequestMapping("/feedbackUs.page")
    public String feedbackUs() {
        return "index/feedbackUs";
    }

    /**
     * 关于我们.
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.OTHER, subModule = "", describe = "关于我们页面")
    @RequestMapping("/aboutUs.page")
    public String aboutUs() {
        return "index/aboutUs";
    }

    /**
     * easyUI Demo.
     */
    @OperationLogAnnotation(type = OperationEnum.PAGE, module = ModuleEnum.OTHER, subModule = "", describe = "easyUI Demo")
    @RequestMapping("/easyUIDemo.page")
    public String easyUI() {
        return "index/easyuiDemo";
    }

    /**
     * 对密码做特殊处理.
     *
     * @param password 密文
     * @return 密文
     */
    private String hidePassword(String password) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(password)) {
            for (int i = 0; i < password.length() - 1; i++) {
                if (i % 3 == 0) {
                    sb.append("*");
                } else {
                    sb.append(password.charAt(i));
                }
            }
        }
        return sb.toString();
    }
}

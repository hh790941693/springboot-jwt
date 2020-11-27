package com.pjb.springbootjwt.zhddkk.controller;

import com.pjb.springbootjwt.zhddkk.bean.SessionInfoBean;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * session信息控制器
 */
@Controller
public class SessionController {
	/**
	 * 查看session信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/sessionInfo")
	@ResponseBody
	public void querySessionInfo(HttpServletRequest request, HttpServletResponse response){
		HttpSession httpSession = request.getSession(false);
		Enumeration<String> enumeration = httpSession.getAttributeNames();

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter printWriter = null;
		try {
			printWriter = response.getWriter();
			if (enumeration.hasMoreElements()) {
				int maxInactiveInterval = httpSession.getMaxInactiveInterval();
				String sessionId = httpSession.getId();
				printWriter.println("sessionId : " + sessionId);
				printWriter.println("maxInactiveInterval : " + maxInactiveInterval);
			}
			while (enumeration.hasMoreElements()){
				String name = enumeration.nextElement();
				Object value = null;
				if (name.equals(CommonConstants.SESSION_INFO)){
					SessionInfoBean sessionInfoBean = (SessionInfoBean)httpSession.getAttribute(name);
					value = JsonUtil.javaobject2Jsonstr(sessionInfoBean);
				} else {
					value = (String) httpSession.getAttribute(name);
				}
				String outputStr = name + " : " + value;
				printWriter.println(outputStr);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 跳转session信息页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/sessionInfo.page")
	public String sessionInfoPage(HttpServletRequest request, Model model){
		HttpSession httpSession = request.getSession(false);
		SessionInfoBean sessionInfoBean = (SessionInfoBean)httpSession.getAttribute(CommonConstants.SESSION_INFO);

		if (null != sessionInfoBean) {
			SessionInfoBean copyBean = new SessionInfoBean();
			BeanUtils.copyProperties(sessionInfoBean, copyBean);
			String encryPassword = hidePassword(sessionInfoBean.getPassword());
			copyBean.setPassword(encryPassword);
			model.addAttribute(CommonConstants.SESSION_INFO, JsonUtil.javaobject2Jsonobject(copyBean));
			return "ws/sessionInfo";
		}

		return "redirect:/";
	}

	/**
	 * 对密码做特殊处理
	 *
	 * @param password 密文
	 * @return
	 */
	private String hidePassword(String password){
		StringBuffer sb = new StringBuffer();
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
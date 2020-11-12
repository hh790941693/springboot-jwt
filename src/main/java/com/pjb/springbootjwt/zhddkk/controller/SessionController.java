package com.pjb.springbootjwt.zhddkk.controller;

import com.pjb.springbootjwt.zhddkk.bean.SessionInfoBean;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.util.JsonUtil;
import org.springframework.stereotype.Controller;
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
}
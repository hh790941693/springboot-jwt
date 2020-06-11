package com.pjb.springbootjwt.zhddkk.controller;

import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.util.SecurityAESUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页
 */
@Controller
public class HomeController {

	/**
	 * 首页登录
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping({"","/index"})
	public String v_home(Model model, HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			boolean isAdmin = false;
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(CommonConstants.S_USER) && cookie.getMaxAge() != 0) {
					model.addAttribute(CommonConstants.S_USER, cookie.getValue());
					if (cookie.getValue().equals("admin")){
						isAdmin = true;
					}
				}else if (cookie.getName().equals(CommonConstants.S_PASS) && cookie.getMaxAge() != 0) {
					if (isAdmin){
						//不保存admin密码
						model.addAttribute(CommonConstants.S_PASS, "");
						cookie.setMaxAge(0);
						response.addCookie(cookie);
					}else {
						//对密码进行解密
						String passDecrypt = SecurityAESUtil.decryptAES(cookie.getValue(), CommonConstants.AES_PASSWORD);
						model.addAttribute(CommonConstants.S_PASS, passDecrypt);
					}
				}
			}
		}else{
			model.addAttribute(CommonConstants.S_USER, "");
			model.addAttribute(CommonConstants.S_PASS, "");
		}
		return "ws/login";
	}

	/**
	 * 查看session信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/sessionInfo")
	@ResponseBody
	public void querySessionInfo(HttpServletRequest request, HttpServletResponse response){
		HttpSession httpSession = request.getSession();
		Enumeration<String> enumeration = httpSession.getAttributeNames();


		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter printWriter = null;
		try {
			printWriter = response.getWriter();
			while (enumeration.hasMoreElements()){
				String name = enumeration.nextElement();
				String value = (String)httpSession.getAttribute(name);
				String outputStr = name+" : "+value;
				printWriter.println(outputStr);
			}
		}catch (Exception e){

		}
	}
}

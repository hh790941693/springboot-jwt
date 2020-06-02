package com.pjb.springbootjwt.zhddkk.controller;

import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.util.SecurityAESUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 首页
 */
@Controller
public class HomeController {
	
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
}

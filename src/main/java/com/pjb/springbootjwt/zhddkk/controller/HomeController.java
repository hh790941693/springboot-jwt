package com.pjb.springbootjwt.zhddkk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页
 */
@Controller
public class HomeController {
	
	@RequestMapping({"","/index"})
	public String v_home() {
		return "ws/login";
	}
}

package com.jscxrz.zhddkk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/ws")
public class HomeController {
	
	@RequestMapping("/index")
	public String v_home() {
		return "ws/login";
	}

	@GetMapping("/test")
	@ResponseBody
	public String test(){
		return "hello world";
	}
}

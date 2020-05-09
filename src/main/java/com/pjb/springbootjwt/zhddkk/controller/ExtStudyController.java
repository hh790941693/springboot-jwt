package com.pjb.springbootjwt.zhddkk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ExtJS学习用控制器
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("extjs")
public class ExtStudyController 
{
	@RequestMapping("index.page")
	public String myFirstExtPro()
	{
		return "extjs/index";
	}
}

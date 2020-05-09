package com.jscxrz.zhddkk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("arcgis")
public class ArcgisController {

	@RequestMapping("arcgisDemo")
	public String v_arcgisDemo() {
		
		return "arcgis/arcgisDemo";
	}
}

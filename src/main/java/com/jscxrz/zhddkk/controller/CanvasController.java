package com.jscxrz.zhddkk.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jscxrz.zhddkk.annotation.OperationLogAnnotation;
import com.jscxrz.zhddkk.enumx.ModuleEnum;
import com.jscxrz.zhddkk.enumx.OperationEnum;

@Controller
@RequestMapping("canvas")
public class CanvasController {
	
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.TWOD_GAME,subModule="",describe="2D游戏首页")
	@RequestMapping("canvasIndex.page")
	public String canvasIndex() {
		return "canvas/canvasIndex";
	}
	
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.TWOD_GAME,subModule="",describe="某某2D游戏首页")
	@RequestMapping("{gameName}.page")
	public String xcanvasIndex(@PathVariable("gameName") String gameName,HttpServletRequest req) {
		return "canvas/" + gameName;
	}
}

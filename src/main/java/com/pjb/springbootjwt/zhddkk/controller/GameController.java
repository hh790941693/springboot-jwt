package com.pjb.springbootjwt.zhddkk.controller;

import javax.servlet.http.HttpServletRequest;

import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.enumx.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.enumx.OperationEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("game")
public class GameController 
{
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.GAME,subModule="",describe="JS游戏首页")
	@RequestMapping(value="gameIndex.page",method=RequestMethod.POST)
	public String gameIndex() {
		return "game/gameIndex";
	}
	
	@OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.GAME,subModule="",describe="某某游戏首页")
	@RequestMapping("{gameName}.page")
	public String xgameIndex(@PathVariable("gameName") String gameName,HttpServletRequest req) {
		return "game/" + gameName;
	}	
}

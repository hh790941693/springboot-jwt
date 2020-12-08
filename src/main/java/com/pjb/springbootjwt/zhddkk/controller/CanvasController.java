package com.pjb.springbootjwt.zhddkk.controller;

import javax.servlet.http.HttpServletRequest;

import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.constants.ModuleEnum;
import com.pjb.springbootjwt.zhddkk.constants.OperationEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("canvas")
public class CanvasController {

    @OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.TWOD_GAME,subModule="",describe="canvas游戏首页")
    @RequestMapping("canvasIndex.page")
    public String canvasIndex() {
        return "canvas/canvasIndex";
    }

    @OperationLogAnnotation(type=OperationEnum.PAGE,module=ModuleEnum.TWOD_GAME,subModule="",describe="某某canvas游戏首页")
    @RequestMapping("{gameName}.page")
    public String xcanvasIndex(@PathVariable("gameName") String gameName,HttpServletRequest req) {
        return "canvas/" + gameName;
    }
}
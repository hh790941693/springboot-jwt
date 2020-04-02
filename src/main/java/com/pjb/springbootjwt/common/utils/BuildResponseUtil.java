package com.pjb.springbootjwt.common.utils;

import com.pjb.springbootjwt.common.vo.Result;
import com.pjb.springbootjwt.util.JSONUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class BuildResponseUtil {

    public static void buildRes(String code, String desc, HttpServletResponse httpServletResponse){
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try(PrintWriter writer = httpServletResponse.getWriter()){
            Result result = Result.build(Integer.valueOf(code), desc);
            writer.write(JSONUtils.beanToJson(result));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

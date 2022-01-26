package com.pjb.springbootjwt.zhddkk.exception;


import com.pjb.springbootjwt.zhddkk.base.Result;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 缺少请求参数或参数格式错误
     * body为空或Json格式错误
     */
    @ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class})
    @ResponseBody
    public Result<String> apiAuthorizationException(HttpMessageNotReadableException e) {
        return Result.build(-1, e.getMessage(), e.getMessage());
    }

    /**
     * 参数校验失败 @Validated
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Result<String> BindExceptionHandler(BindException e, HttpServletRequest request) {
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(","));

        // 返回一个页面视图
        return Result.build(Integer.valueOf(CommonConstants.REQUEST_ERROR_CODE), message, "requestError");
    }

    /**
     * 参数校验失败 @NotBlank @Email
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public Result<String> constraintViolationExceptionHandler(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
        return Result.build(-3, message, message);
    }

    /**
     * 参数校验失败 RequestBody
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(","));
        return Result.build(-4, message, message);
    }

    /**
     * 捕捉系统异常
     */
    @ExceptionHandler({RuntimeException.class, Exception.class})
    public Object exceptionHandler(Exception e, HttpServletRequest request){
        e.printStackTrace();
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("errorMsg", e.getMessage());
        mav.addObject("url", request.getRequestURL());
        mav.addObject("redirectName", "exception");
        mav.setViewName("ws/exception");
        // 返回一个页面视图
        return mav;
    }
}

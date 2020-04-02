package com.pjb.springbootjwt.common.exception;

import com.pjb.springbootjwt.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 *
 */
@RestControllerAdvice
public class ApplicationExceptionHandler {
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 自定义异常
     */
    @ExceptionHandler(ApplicationException.class)
    public Result<String> handleApplicationException(ApplicationException e) {
        log.info("出现RuntimeException: "+e.getMessage());
        int code = Integer.parseInt(e.getCode());
        return Result.build(code, e.getDesc());
    }
}


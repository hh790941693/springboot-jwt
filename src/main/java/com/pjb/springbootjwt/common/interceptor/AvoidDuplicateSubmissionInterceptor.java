package com.pjb.springbootjwt.common.interceptor;

import com.pjb.springbootjwt.common.annotation.SubToken;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.UUID;

public class AvoidDuplicateSubmissionInterceptor extends
        HandlerInterceptorAdapter {

    public AvoidDuplicateSubmissionInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            SubToken annotation = method
                    .getAnnotation(SubToken.class);
            if (annotation != null) {
                boolean needSaveSession = annotation.save();
                if (needSaveSession) {
                    request.getSession(false).setAttribute("subToken", generateToken());
                }

                boolean needRemoveSession = annotation.remove();
                if (needRemoveSession) {
                    if (isRepeatSubmit(request)) {
                        System.out.println("重复提交!");
                        return false;
                    }
                    request.getSession(false).removeAttribute("subToken");
                }
            }
        }
        return true;
    }

    /**
     * 是否重复提交请求.
     * @param request
     * @return
     */
    private boolean isRepeatSubmit(HttpServletRequest request) {
        String serverToken = (String) request.getSession(false).getAttribute(
                "subToken");
        if (serverToken == null) {
            return true;
        }
        String clinetToken = request.getParameter("subToken");
        if (clinetToken == null) {
            return true;
        }
        if (!serverToken.equals(clinetToken)) {
            return true;
        }
        return false;
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replaceAll("\\-", "");
    }
}

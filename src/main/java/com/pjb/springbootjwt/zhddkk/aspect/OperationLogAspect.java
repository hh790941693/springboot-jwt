package com.pjb.springbootjwt.zhddkk.aspect;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation;
import com.pjb.springbootjwt.zhddkk.bean.SessionInfoBean;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.domain.WsOperationLogDO;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import com.pjb.springbootjwt.zhddkk.service.WsOperationLogService;
import com.pjb.springbootjwt.zhddkk.service.WsUsersService;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import com.pjb.springbootjwt.zhddkk.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 操作日志切面.
 */
@Aspect
@Order(1)
@Component
public class OperationLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    @Autowired
    private WsUsersService wsUsersService;

    @Autowired
    private WsOperationLogService wsOperationLogService;

    @Pointcut("@annotation(com.pjb.springbootjwt.zhddkk.annotation.OperationLogAnnotation)")
    public void operationLogPointcut() {
    }

    /**
     * 环切.
     * @param joinPoint 切入点
     * @return 结果
     * @throws Throwable 异常
     */
    @Around("operationLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        WsOperationLogDO wol = new WsOperationLogDO();
        wol.setAccessTime(new Date());
        wol.setCreateTime(new Date());

        //执行方法之前的时间戳
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = joinPoint.proceed();
        //响应时间
        Long costTime = System.currentTimeMillis() - beginTime;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestUrl = request.getRequestURL().toString();
        String remoteAddr = request.getRemoteAddr();

        SessionInfoBean sessionInfoBean = SessionUtil.getSessionAttribute(CommonConstants.SESSION_INFO);
        String userName = sessionInfoBean == null ? "" : sessionInfoBean.getUserName();

        if (StringUtils.isNotBlank(userName)) {
            WsUsersDO wsUsersDO = wsUsersService.selectOne(new EntityWrapper<WsUsersDO>().eq("name", userName));
            wol.setUserName(userName);
            wol.setUserId(wsUsersDO.getId());
        }
        //获得类名
        String className = joinPoint.getTarget().getClass().getName();
        //获得方法名
        String methodName = joinPoint.getSignature().getName();
        //获得注解方法的参数数组
        Object[] params = joinPoint.getArgs();

        String resultString = "";
        if (null != result) {
            resultString = result.toString();
            if (resultString.length() > 60000) {
                resultString = resultString.substring(0, 60000);
            }
        }
        wol.setOperResult(resultString);
        wol.setCostTime(costTime.intValue());
        wol.setRequestUrl(requestUrl);
        wol.setClassName(className);
        wol.setMethodName(methodName);

        //获得类
        Class<?> targetClass = Class.forName(className);
        //获取类下面所有非私有的方法
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (methodName.equals(method.getName())) {
                //获得方法的参数类型数组
                Class<?>[] classArr = method.getParameterTypes();
                if (params.length == classArr.length) { //判断参数长度是否一样
                    //获得日志注解
                    OperationLogAnnotation operationLog = method.getAnnotation(OperationLogAnnotation.class);
                    if (null == operationLog) {
                        continue;
                    }
                    wol.setOperModule(operationLog.module().getValue());
                    wol.setOperType(operationLog.type().getValue());
                    wol.setOperSubmodule(operationLog.subModule());
                    wol.setOperDescribe(operationLog.describe());
                    wol.setOperRemark(operationLog.remark() + " IP:" + remoteAddr);

                    Parameter[] parameters = method.getParameters();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter p = parameters[i];
                        String paraterName = p.getName();
                        Class<?> paraterType = p.getType();
                        if (paraterType == org.springframework.ui.Model.class || paraterType == HttpServletRequest.class
                                    || paraterType == javax.servlet.http.HttpServletResponse.class) {
                            continue;
                        }

                        Object parameterValue = params[i];
                        if (null == parameterValue) {
                            continue;
                        }

                        String parameterValueStr = parameterValue.toString();
                        if (paraterName.contains(CommonConstants.C_PASS)) {
                            parameterValueStr = "******";
                        }
                        if (parameterValueStr.length() >= 300) {
                            parameterValueStr = parameterValueStr.substring(0, 298);
                        }
                        sb.append(paraterName).append(":").append(parameterValueStr).append(" ");
                    }
                    wol.setParameters(sb.toString());
                    break;
                }
            }
        }

        try {
            boolean insertFlag = wsOperationLogService.insert(wol);
            if (insertFlag) {
                logger.info("新增操作日志成功:" + wol.getOperDescribe() + " 操作人:" + userName);
            }
        } catch (Exception e) {
            logger.error("记录操作日志失败:" + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
}

package com.jscxrz.zhddkk.aspect;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jscxrz.zhddkk.annotation.OperationLogAnnotation;
import com.jscxrz.zhddkk.entity.WsOperationLog;
import com.jscxrz.zhddkk.entity.WsUser;
import com.jscxrz.zhddkk.service.WsService;

@Aspect
@Order(1)
@Component
public class OperationLogAspect {

	private static final Log logger = LogFactory.getLog(OperationLogAspect.class);
	
	@Autowired
	private WsService wsService; 
	
    @Pointcut("@annotation(com.jscxrz.zhddkk.ws.annotation.OperationLogAnnotation)")
    public void operationLogPointcut() {
    	
    }
    
    @Around("operationLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    	WsOperationLog wol = new WsOperationLog();
    	wol.setAccessTime(new Date());
    	
        //执行方法之前的时间戳
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = joinPoint.proceed();
        //响应时间
        Long costTime = System.currentTimeMillis() - beginTime;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //String requestUri = request.getRequestURI();
        String requestUrl = request.getRequestURL().toString();
        String userName = (String)request.getSession().getAttribute("user");

        if (null != userName) {
			Integer userId = querySpecityUserName(userName).getId();
			wol.setUserName(userName);
			wol.setUserId(userId);
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
	        if (resultString.length()>60000) {
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
                	wol.setOperModule(operationLog.module().getValue());
                	wol.setOperType(operationLog.type().getValue());
                	wol.setOperSubmodule(operationLog.subModule());
                	wol.setOperDescribe(operationLog.describe());
                	wol.setOperRemark(operationLog.remark());
                	
                	Parameter[] parameters = method.getParameters();
                	StringBuilder sb = new StringBuilder();
                	for (int i=0;i<parameters.length;i++) {
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
                		if (paraterName.contains("pass")) {
                			parameterValueStr = "******";
                		}
                		if (parameterValueStr.length()>=300) {
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
        	wol.setCreateTime(new Date());
        	wsService.insertOperationLog(wol);
        }catch (Exception e) {
        	logger.error("记录操作日志失败:"+e.getMessage());
        	e.printStackTrace();
        }
        
        return result;
    }
    
	/**
	 * 根据用户名称查询用户信息
	 * 
	 * @param name
	 * @return
	 */
	private WsUser querySpecityUserName(String name) {
		WsUser wu = new WsUser();
		wu.setName(name);
		List<WsUser> wuList = wsService.queryWsUser(wu);
		if (wuList.size() > 0) {
			return wuList.get(0);
		}
		return null;
	}
}

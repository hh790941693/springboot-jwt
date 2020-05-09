package com.jscxrz.zhddkk.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jscxrz.zhddkk.constants.CommonConstants;
import com.jscxrz.zhddkk.entity.WsUser;

@Service
public class BaseService {
	
	@Autowired
	private HttpServletRequest request;
	
	public void setSessionUser(WsUser user) {
		request.getSession().setAttribute(CommonConstants.SESSION_USER, user);
	}
	
	public WsUser getSessionUser() {
		Object obj = request.getSession().getAttribute(CommonConstants.SESSION_USER);
		if (null != obj)
		{
			WsUser user = (WsUser)obj;
			return user;
		}
		
		return null;
	}
}

package com.pjb.springbootjwt.zhddkk.service.impl;

import javax.servlet.http.HttpServletRequest;

import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.entity.WsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

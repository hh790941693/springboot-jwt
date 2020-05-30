package com.pjb.springbootjwt.zhddkk.service.impl;

import javax.servlet.http.HttpServletRequest;
import com.pjb.springbootjwt.zhddkk.constants.CommonConstants;
import com.pjb.springbootjwt.zhddkk.domain.WsUsersDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseService {
	
	@Autowired
	private HttpServletRequest request;
	
	public void setSessionUser(WsUsersDO user) {
		request.getSession().setAttribute(CommonConstants.SESSION_USER, user);
	}
	
	public WsUsersDO getSessionUser() {
		Object obj = request.getSession().getAttribute(CommonConstants.SESSION_USER);
		if (null != obj) {
			WsUsersDO user = (WsUsersDO)obj;
			return user;
		}
		return null;
	}
}

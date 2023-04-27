package org.automationlab.api.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
   
	@Autowired
	Environment environment;
	
	@Override
	public boolean preHandle(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Object handler) throws Exception
	{ 
		String secretKey = environment.getProperty("apiKey");
		if(secretKey.equals(request.getHeader("SecretKey"))) {
			return true;
		}
		response.setStatus(401);		
		return false;
	}
}
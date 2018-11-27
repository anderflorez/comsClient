package com.unlimitedcompanies.comsWeb.config;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.unlimitedcompanies.comsWeb.appManagement.UserSessionManager;

public class RequestSessionCheckInterceptor extends HandlerInterceptorAdapter
{	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	{
		// Find the session object
		HttpSession httpSession = request.getSession();
		UserSessionManager session = (UserSessionManager) httpSession.getAttribute("scopedTarget.userSessionManager");
		
		if (session ==  null || !session.checkSession())
		{
			// Find out the user initial intended url target
			String contextPath = request.getRequestURL().toString();
			String url = contextPath;
			if (url.endsWith("/"))
			{
				url = url.substring(0, url.length() - 1);
			}
			Enumeration<String> paramNames = request.getParameterNames();
			if (paramNames.hasMoreElements())
			{
				String param = paramNames.nextElement();
				url += "?" + param + "=" + request.getParameter(param);
			}
			while(paramNames.hasMoreElements())
			{
				String param = paramNames.nextElement();
				url += "&" + param + "=" + request.getParameter(param);
			}

			// Save the initial user intended url target in the session
			request.getSession().setAttribute("initial_request", url);
						
			try
			{	
				// Start the process to get authenticated with OAuth - Request authorization code
				response.sendRedirect("http://localhost:8080/comsws/oauth/authorize"
						+ "?response_type=code"
						+ "&client_id=comsClient"
						+ "&redirect_uri=" + contextPath + "tokenmanager");
			} 
			catch (IOException e)
			{
				// TODO: Log the issue and provide an error to the user instead of printing the stack trace
				e.printStackTrace();
			}
			return false;
		}
		else 
		{
			return true;
		}
		
	}
}

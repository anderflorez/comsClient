package com.unlimitedcompanies.comsWeb.config;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class RequestSessionCheckInterceptor extends HandlerInterceptorAdapter
{
	@Autowired
	UserSessionManager session;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	{
		if (session == null)
		{
			System.out.println("===================================================\n\n");
			System.out.println("Running interceptor - no session found");
			System.out.println("\n\n===================================================");
			
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
			
			try
			{
				System.out.println("===================================================\n\n");
				System.out.println("returning false and redirecting to /sessioncreator?redirect_url=" + url);
				System.out.println("\n\n===================================================");
				
				response.sendRedirect(contextPath + "sessioncreator");
			} 
			catch (IOException e)
			{
				// TODO: Log the issue and provide an error to the user instead of printing the stack trace
				e.printStackTrace();
			}
			return false;
			
		}
//		else if (!session.checkSession())
//		{
//			System.out.println("===================================================\n\n");
//			System.out.println("Running interceptor - found invalid session");
//			System.out.println("\n\n===================================================");
//			
//			String contextPath = request.getRequestURL().toString(); 
//			String url = contextPath;
//			if (url.endsWith("/"))
//			{
//				url = url.substring(0, url.length() - 1);
//			}
//			Enumeration<String> paramNames = request.getParameterNames();
//			if (paramNames.hasMoreElements())
//			{
//				String param = paramNames.nextElement();
//				url += "?" + param + "=" + request.getParameter(param);
//			}
//			while(paramNames.hasMoreElements())
//			{
//				String param = paramNames.nextElement();
//				url += "&" + param + "=" + request.getParameter(param);
//			}
//						
//			try
//			{
//				System.out.println("===================================================\n\n");
//				System.out.println("returning false and redirecting to comsws/oauth/authorize");
//				System.out.println("\n\n===================================================");
//				
//				session.setInitialRequest(url);
//				
//				response.sendRedirect("http://localhost:8080/comsws/oauth/authorize"
//						+ "?response_type=code"
//						+ "&client_id=comsClient"
//						+ "&redirect_uri=" + contextPath + "tokenmanager");
//			} 
//			catch (IOException e)
//			{
//				// TODO: Log the issue and provide an error to the user instead of printing the stack trace
//				e.printStackTrace();
//			}
//			return false;
//		}
		else 
		{
			System.out.println("===================================================\n\n");
			System.out.println("Running interceptor - Valid session found - returning true");
			System.out.println("\n\n===================================================");
			return true;
		}
		
	}
}

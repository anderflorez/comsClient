package com.unlimitedcompanies.comsClient.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unlimitedcompanies.comsClient.config.NullTokenException;
import com.unlimitedcompanies.comsClient.config.UserSessionManager;

@Controller
public class DashboardController
{
	@Autowired
	UserSessionManager session;
	
	@RequestMapping("/")
	public ModelAndView showDashboard()
	{
		ModelAndView mv = new ModelAndView();
		try
		{
			String token = session.getToken();
			mv.setViewName("/pages/dashboard/dashboard.jsp");
			mv.addObject("clientUser", session.getUsername());
		} 
		catch (NullTokenException e)
		{
			mv.setViewName(session.getAuthCodeRedirectUrl("/"));
		}
		return mv;
	}
}

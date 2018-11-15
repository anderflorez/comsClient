package com.unlimitedcompanies.comsClient.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unlimitedcompanies.comsClient.config.NullOrIncompleteSessionException;
import com.unlimitedcompanies.comsClient.config.UserSessionManager;
import com.unlimitedcompanies.comsClient.security.representations.LoggedUserInfo;

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
			LoggedUserInfo loggedUserInfo = new LoggedUserInfo(session.getUsername(), 
															   session.getUserFirstName(), 
															   session.getUserLastName());
			mv.setViewName("/pages/dashboard/dashboard.jsp");
			mv.addObject("loggedUser", loggedUserInfo);
		} 
		catch (NullOrIncompleteSessionException e)
		{
			mv.setViewName(session.getAuthCodeRedirectUrl("/"));
		}
		return mv;
	}
}

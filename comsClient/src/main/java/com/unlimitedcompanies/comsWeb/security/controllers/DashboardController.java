package com.unlimitedcompanies.comsWeb.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unlimitedcompanies.comsWeb.config.NullOrIncompleteSessionException;
import com.unlimitedcompanies.comsWeb.config.UserSessionManager;

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
			mv.setViewName("/pages/security/dashboard.jsp");
			mv.addObject("loggedUser", session.getLogedUserFullName());
		} 
		catch (NullOrIncompleteSessionException e)
		{
			mv.setViewName(session.getAuthCodeRedirectUrl("/"));
		}
		return mv;
	}
}

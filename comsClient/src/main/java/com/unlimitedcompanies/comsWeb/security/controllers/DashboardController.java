package com.unlimitedcompanies.comsWeb.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unlimitedcompanies.comsWeb.appManagement.UserSessionManager;

@Controller
public class DashboardController
{
	@Autowired
	UserSessionManager session;
	
	@RequestMapping("/")
	public ModelAndView showDashboard()
	{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("dashboard");
		mv.addObject("loggedUser", session.getLogedUserFullName());
		return mv;
	}
}

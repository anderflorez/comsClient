package com.unlimitedcompanies.comsWeb.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
		mv.setViewName("/pages/security/dashboard.jsp");
		mv.addObject("loggedUser", session.getLogedUserFullName());
		return mv;
	}
	
	@RequestMapping("/sessioncreator")
	public ModelAndView createSession()
	{
		System.out.println("===================================================\n\n");
		System.out.println("in /sessionCreator session token " + session.getToken());
		System.out.println("\n\n===================================================");
		return new ModelAndView("redirect:/");
	}
}

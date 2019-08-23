package com.unlimitedcompanies.comsWeb.controllers.policies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.unlimitedcompanies.comsWeb.appManagement.UserSessionManager;

@Controller
public class PolicyDisplayController
{
	@Autowired
	UserSessionManager session;
	
	@RequestMapping(value = "/policies", method = RequestMethod.GET)
	public ModelAndView displayAllPolicies()
	{
		ModelAndView mv = new ModelAndView("policies/policyList");
		
		
		
		return mv;
	}
}

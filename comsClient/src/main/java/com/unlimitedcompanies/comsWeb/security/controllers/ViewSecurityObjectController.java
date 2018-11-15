package com.unlimitedcompanies.comsWeb.security.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.unlimitedcompanies.comsWeb.config.NullOrIncompleteSessionException;
import com.unlimitedcompanies.comsWeb.config.UserSessionManager;
import com.unlimitedcompanies.comsWeb.security.representations.Contact;

@Controller
public class ViewSecurityObjectController
{
	@Autowired
	UserSessionManager session;
	
	@RequestMapping("/contacts")
	public ModelAndView showContacts(@RequestParam(name = "error", required = false) String error)
	{
		ModelAndView mv = new ModelAndView("/pages/security/contactView.jsp");
		try
		{
			List<Contact> allContacts = null;
			mv.addObject("contacts", allContacts);
			mv.addObject("loggedUser", session.getLogedUserFullName());
		} 
		catch (NullOrIncompleteSessionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (error != null)
		{
			mv.addObject("error", error);
		}
		
		return mv;
	}
}

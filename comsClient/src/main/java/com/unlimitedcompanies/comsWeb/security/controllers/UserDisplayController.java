package com.unlimitedcompanies.comsWeb.security.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.unlimitedcompanies.comsWeb.appManagement.LinkManager;
import com.unlimitedcompanies.comsWeb.appManagement.LinkNotFoundException;
import com.unlimitedcompanies.comsWeb.appManagement.UserSessionManager;
import com.unlimitedcompanies.comsWeb.security.representations.Contact;
import com.unlimitedcompanies.comsWeb.security.representations.ErrorMessages;
import com.unlimitedcompanies.comsWeb.security.representations.User;
import com.unlimitedcompanies.comsWeb.security.representations.UserCollection;

@Controller
public class UserDisplayController
{
	@Autowired
	UserSessionManager session;
	
	@Autowired
	LinkManager links;
	
	@RequestMapping(value = "/users")
	public ModelAndView displayAllUsers(HttpServletRequest request,
										@RequestParam(name = "errors", required = false) List<String> errors,
										@RequestParam(name = "success", required = false) String success,
										@RequestParam(name = "pag", required = false) Integer pag,
										@RequestParam(name = "epp", required = false) Integer epp)
	{
		ModelAndView mv = new ModelAndView("userList");
		
		String url = null;
		try
		{
			url = links.getBaseLink("base_user") + "s";
		}
		catch (LinkNotFoundException e)
		{
			mv.setViewName("redirect:/");
			List<String> errorList = new ArrayList<>();
			if (errors != null && !errors.isEmpty()) errorList.addAll(errors);
			errorList.add("Error: The request to display a user is invalid");
			mv.addObject("errors", errorList);
			
			return mv;
		}
		
		if (pag != null)
		{
			url += "?pag=" + pag;
			if (epp != null) url += "&epp=" + epp;
		}
		
		// TODO: Improve the address hard coding if possible
		Response response = ClientBuilder.newClient()
										 .target(url)
										 .request()
										 .header("Authorization", "Bearer " + session.getToken())
										 .get();
		
		UserCollection users = response.readEntity(UserCollection.class);
		
		mv.addObject("users", users.getUsers());
		
		if (errors != null)
		{
			mv.addObject("errors", errors);
		}
		
		if (success != null)
		{
			mv.addObject("success", success);
		}
		
		if (users.getPrevPage() != null)
		{
			mv.addObject("previous", request.getContextPath() + "/users?pag=" + users.getPrevPage());
		}
		
		if (users.getNextPage() != null)
		{
			mv.addObject("next", request.getContextPath() + "/users?pag=" + users.getNextPage());
		}
		
		mv.addObject("loggedUser", session.getLogedUserFullName());
		return mv;
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ModelAndView findUserDetails(@RequestParam("uid") Integer id)
	{
		ModelAndView mv = new ModelAndView("userDetails");
		
		try
		{
			Response response = ClientBuilder.newClient()
											 .target(links.getBaseLink("base_user") + "/" + id)
											 .request()
											 .header("Authorization", "Bearer " + session.getToken())
											 .get();
			
			if (response.getStatus() == HttpStatus.OK.value())
			{
				User user = response.readEntity(User.class);
				mv.addObject("loggedUser", session.getLogedUserFullName());
				mv.addObject("user", user);
				
				Response contactResponse = ClientBuilder.newClient()
													 .target(links.getBaseLink("base_contact") + "/" + user.getContactId())
													 .request()
													 .header("Authorization", "Bearer " + session.getToken())
													 .get();
				
				if (contactResponse.getStatus() == HttpStatus.OK.value())
				{
					Contact contact = contactResponse.readEntity(Contact.class);
					mv.addObject("contact", contact);
				}
			}
			else
			{
				ErrorMessages error = response.readEntity(ErrorMessages.class);
				mv.setViewName("redirect:/users");
				mv.addObject("errors", error.getErrors());
				mv.addObject("messages", error.getMessages());
			}
		} 
		catch (LinkNotFoundException e)
		{
			mv.setViewName("redirect:/users");
			List<String> errors = new ArrayList<>();
			errors.add("Error: The request to display a user is invalid");
			mv.addObject("errors", errors);
		}
		
		return mv;

	}
}

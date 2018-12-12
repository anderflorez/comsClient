package com.unlimitedcompanies.comsWeb.security.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
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

@Controller
public class UserManagementController
{
	@Autowired
	UserSessionManager session;

	@Autowired
	LinkManager links;

	@RequestMapping(value = "/userManagement", method = RequestMethod.GET)
	public ModelAndView displayUserForm(@RequestParam(name = "uid", required = false) Integer uid,
										@RequestParam(name = "cid", required = false) Integer cid)
	{
		ModelAndView mv = new ModelAndView("userForm");

		if (uid == null)
		{
			// This is a request for the form to create a new user
			if (cid != null)
			{
				User user = new User();
				user.setContactId(cid);
				mv.addObject("user", user);
			}
		}
		else
		{
			// This is a request for the form to update an existing user
			try
			{
				Response response = ClientBuilder.newClient().target(links.getBaseLink("user") + uid).request()
						.header("Authorization", "Bearer " + session.getToken()).get();

				if (response.getStatus() == HttpStatus.OK.value())
				{
					User user = response.readEntity(User.class);
					mv.addObject("user", user);
				}
				else
				{
					ErrorMessages errors = response.readEntity(ErrorMessages.class);
					mv.setViewName("redirect:/users");
					mv.addObject("errors", errors.getErrors());
					mv.addObject("messages", errors.getMessages());
				}
			}
			catch (LinkNotFoundException e)
			{
				mv.setViewName("redirect:/users");
				List<String> errors = new ArrayList<>();
				errors.add("Error: The request to edit the user is invalid, please try again");
				mv.addObject("errors", errors);
			}
		}

		mv.addObject("loggedUser", session.getLogedUserFullName());
		return mv;
	}
	
	@RequestMapping(value = "/userManagement", method = RequestMethod.POST)
	public ModelAndView displayUserForm(User user)
	{
		ModelAndView mv = new ModelAndView("userDetails");
		
		if (user.getUserId() == null && user.getContactId() != null)
		{
			// Make a request to create a new user
			
			try
			{
				System.out.println("==========> Start request by getting a webtarget");
				System.out.println("==========> using link: " + links.getBaseLink("user"));
				WebTarget target = ClientBuilder.newClient().target(links.getBaseLink("user"));
				Builder builder = target.request().header("Authorization", "Bearer " + session.getToken());
				Response response = builder.post(Entity.json(user));

				
				if (response.getStatus() == HttpStatus.CREATED.value())
				{
					User createdUser = response.readEntity(User.class);
					mv.addObject("loggedUser", session.getLogedUserFullName());
					mv.addObject("user", createdUser);
					
					Response contactResponse = ClientBuilder.newClient()
							 .target(links.getBaseLink("contact") + createdUser.getContactId())
							 .request()
							 .header("Authorization", "Bearer " + session.getToken())
							 .get();

					if (contactResponse.getStatus() == HttpStatus.OK.value())
					{
						Contact contact = contactResponse.readEntity(Contact.class);
						mv.addObject("contact", contact);
					}
				}
			}
			catch (LinkNotFoundException e)
			{
				mv.setViewName("redirect:/contact?cid=" + user.getContactId());
				List<String> errors = new ArrayList<>();
				errors.add("An internal error has occurred, please try again or contact your system administrator");
				mv.addObject("errors", errors);
				return mv;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (user.getUserId() != null && user.getContactId() != null)
		{
			// Make a request to update an existing user
			try
			{
				Response response = ClientBuilder.newClient()
						 .target(links.getBaseLink("contact"))
						 .request()
						 .header("Authorization", "Bearer " + session.getToken())
						 .put(Entity.json(user));
				
				if (response.getStatus() == HttpStatus.OK.value())
				{
					User createdUser = response.readEntity(User.class);
					mv.addObject("loggedUser", session.getLogedUserFullName());
					mv.addObject("user", createdUser);
					
					Response contactResponse = ClientBuilder.newClient()
							 .target(links.getBaseLink("contact") + createdUser.getContactId())
							 .request()
							 .header("Authorization", "Bearer " + session.getToken())
							 .get();

					if (contactResponse.getStatus() == HttpStatus.OK.value())
					{
						Contact contact = contactResponse.readEntity(Contact.class);
						mv.addObject("contact", contact);
					}
				}
			}
			catch (LinkNotFoundException e)
			{
				mv.setViewName("redirect:/users");
				List<String> errors = new ArrayList<>();
				errors.add("An internal error has occurred, please try again or contact your system administrator");
				mv.addObject("errors", errors);
				return mv;
			}
		}
		else
		{
			// This is a wrong request
			// TODO: Send an error message back to the user with an http status code for bad request
		}
		
		return mv;
	}
}

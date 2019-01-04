package com.unlimitedcompanies.comsWeb.security.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
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
import com.unlimitedcompanies.comsWeb.security.representations.UserPassword;

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
				Response response = ClientBuilder.newClient()
										.target(links.getBaseLink("base_user") + "/" + uid)
										.request()
										.header("Authorization", "Bearer " + session.getToken())
										.get();

				if (response.getStatus() == HttpStatus.OK.value())
				{
					User user = response.readEntity(User.class);
					mv.addObject("user", user);
				}
				else
				{
					if (response.getHeaderString("comsAPI") != null)
					{
						ErrorMessages errors = response.readEntity(ErrorMessages.class);
						mv.setViewName("redirect:/users");
						mv.addObject("errors", errors.getErrors());
						mv.addObject("messages", errors.getMessages());
					}
					else
					{
						List<String> errors = new ArrayList<>();
						errors.add("Unknown error");
						errors.add("Error code: " + response.getStatus());
						mv.setViewName("redirect:/users");
						mv.addObject("errors", errors);
					}
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
			
			if (Arrays.equals(user.getPassword(), user.getPasswordcheck()))
			{
				try
				{
					Response response = ClientBuilder.newClient()
							.target(links.getBaseLink("base_user"))
							.request()
							.header("Authorization", "Bearer " + session.getToken())
							.post(Entity.json(user));
					
					
					if (response.getStatus() == HttpStatus.CREATED.value())
					{
						User createdUser = response.readEntity(User.class);
						mv.addObject("loggedUser", session.getLogedUserFullName());
						mv.addObject("user", createdUser);
						
						Response contactResponse = ClientBuilder.newClient()
								.target(links.getBaseLink("base_contact") + "/" + createdUser.getContactId())
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
						if (response.getHeaderString("comsAPI") != null)
						{
							ErrorMessages errors = response.readEntity(ErrorMessages.class);
							mv.setViewName("redirect:/users");
							mv.addObject("errors", errors.getErrors());
							mv.addObject("messages", errors.getMessages());
						}
						else
						{
							List<String> errors = new ArrayList<>();
							errors.add("Unknown error");
							errors.add("Error code: " + response.getStatus());
							mv.setViewName("redirect:/users");
							mv.addObject("errors", errors);
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
			}
			else
			{
				List<String> errors = new ArrayList<>();
				errors.add("Error: The passwords do not match");
				mv.setViewName("redirect:/users");
				mv.addObject("errors", errors);
				return mv;				
			}
			
		}
		else if (user.getUserId() != null && user.getContactId() != null)
		{
			// Make a request to update an existing user
			try
			{
				Response response = ClientBuilder.newClient()
						 .target(links.getBaseLink("base_user"))
						 .request()
						 .header("Authorization", "Bearer " + session.getToken())
						 .put(Entity.json(user));
				
				if (response.getStatus() == HttpStatus.OK.value())
				{
					User createdUser = response.readEntity(User.class);
					mv.addObject("loggedUser", session.getLogedUserFullName());
					mv.addObject("user", createdUser);
					
					Response contactResponse = ClientBuilder.newClient()
							 .target(links.getBaseLink("base_contact") + "/" + createdUser.getContactId())
							 .request()
							 .header("Authorization", "Bearer " + session.getToken())
							 .get();

					if (contactResponse.getStatus() == HttpStatus.OK.value())
					{
						Contact contact = contactResponse.readEntity(Contact.class);
						mv.addObject("contact", contact);
					}
					else
					{				
						if (response.getHeaderString("comsAPI") != null)
						{
							ErrorMessages errors = response.readEntity(ErrorMessages.class);
							mv.addObject("errors", errors.getErrors());
							mv.addObject("messages", errors.getMessages());
						}
						else
						{
							List<String> errors = new ArrayList<>();
							errors.add("Unknown error");
							errors.add("Error code: " + response.getStatus());
							mv.addObject("errors", errors);
						}
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
	
	@RequestMapping(value = "/userPasswordChange", method = RequestMethod.POST)
	public ModelAndView changeUserPassword(@RequestParam(name = "userId") int userId, 
										   @RequestParam(name = "oldPassword") char[] oldPassword, 
										   @RequestParam(name = "newPassword") char[] newPassword, 
										   @RequestParam(name = "confirmPassword") char[] confirmPassword)
	{
		UserPassword userPassword = new UserPassword(userId, oldPassword, newPassword, confirmPassword);
		
		ModelAndView mv = new ModelAndView("redirect:/user");
		mv.addObject("uid", userPassword.getUserId());
		
		// TODO: Remove the next printing lines
		System.out.println("=======> password: " + String.valueOf(userPassword.getNewPassword()));
		System.out.println("=======> confirm: " + String.valueOf(userPassword.getConfirmPassword()));
		
		if (!String.valueOf(userPassword.getNewPassword()).equals(String.valueOf(userPassword.getConfirmPassword())))
		{
			List<String> errors = new ArrayList<>();
			errors.add("The passwords do not match");
			mv.addObject("errors", errors);
			return mv;
		}
		
		try
		{
			Response response = ClientBuilder.newClient()
											 .target(links.getBaseLink("base_user") + "/password")
											 .request()
											 .header("Authorization", "Bearer " + session.getToken())
											 .put(Entity.json(userPassword));
			
			if (response.getStatus() == HttpStatus.NO_CONTENT.value())
			{
				String success = "The user password has been changed successfully";
				mv.addObject("success", success);
			}
			else
			{				
				if (response.getHeaderString("comsAPI") != null)
				{
					ErrorMessages errors = response.readEntity(ErrorMessages.class);
					mv.addObject("errors", errors.getErrors());
					mv.addObject("messages", errors.getMessages());
				}
				else
				{
					List<String> errors = new ArrayList<>();
					errors.add("Unknown error");
					errors.add("Error code: " + response.getStatus());
					mv.addObject("errors", errors);
				}
			}
		}
		catch (LinkNotFoundException e)
		{
			List<String> errors = new ArrayList<>();
			errors.add("An internal error has occurred, please try again or contact your system administrator");
			mv.addObject("errors", errors);
			return mv;
		}
		
		return mv;
	}
	
	@RequestMapping(name = "/deleteUser", method = RequestMethod.POST)
	public ModelAndView deleteUser(@RequestParam Integer userId)
	{
		ModelAndView mv = new ModelAndView("redirect:/users");
		try
		{
			Response response = ClientBuilder.newClient()
											 .target(links.getBaseLink("base_user") + "/" + userId)
											 .request()
											 .header("Authorization", "Bearer " + session.getToken())
											 .delete();
			
			if (response.getStatus() == HttpStatus.NO_CONTENT.value())
			{
				mv.addObject("success", "The user has been deleted successfully");
			}
			else
			{				
				if (response.getHeaderString("comsAPI") != null)
				{
					ErrorMessages errors = response.readEntity(ErrorMessages.class);
					mv.setViewName("redirect:/users");
					mv.addObject("errors", errors.getErrors());
					mv.addObject("messages", errors.getMessages());
				}
				else
				{
					List<String> errors = new ArrayList<>();
					errors.add("Unknown error");
					errors.add("Error code: " + response.getStatus());
					mv.setViewName("redirect:/users");
					mv.addObject("errors", errors);
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
		
		return mv;
	}
}

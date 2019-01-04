package com.unlimitedcompanies.comsWeb.security.controllers;

import java.util.ArrayList;
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
import com.unlimitedcompanies.comsWeb.security.representations.ErrorMessages;
import com.unlimitedcompanies.comsWeb.security.representations.Role;

@Controller
public class RoleManagementController
{
	@Autowired
	UserSessionManager session;
	
	@Autowired
	LinkManager links;
	
	@RequestMapping(value = "roleManagement", method = RequestMethod.GET)
	public ModelAndView displayRoleForm(@RequestParam(name = "rid", required = false) Integer rid)
	{
		ModelAndView mv = new ModelAndView("roleForm");
		
		if (rid == null)
		{
			// This is a request for the form to create a new role
			Role role = new Role();
			mv.addObject("role", role);
		}
		else
		{
			// This is a request for the form to edit an existing role
			try
			{
				Response roleResponse = ClientBuilder.newClient()
													 .target(links.getBaseLink("base_role") + "/" + rid)
													 .request()
													 .header("Authorization", "Bearer " + session.getToken())
													 .get();
				
				if (roleResponse.getStatus() == HttpStatus.OK.value())
				{
					Role role = roleResponse.readEntity(Role.class);
					mv.addObject("role", role);
				}
				else
				{
					if (roleResponse.getHeaderString("comsAPI") != null)
					{
						ErrorMessages errors = roleResponse.readEntity(ErrorMessages.class);
						mv.setViewName("redirect:/roles");
						mv.addObject("errors", errors.getErrors());
						mv.addObject("messages", errors.getMessages());
					}
					else
					{
						List<String> errors = new ArrayList<>();
						errors.add("Unknown error");
						errors.add("Error code: " + roleResponse.getStatus());
						mv.setViewName("redirect:/roles");
						mv.addObject("errors", errors);
					}
				}
			}
			catch (LinkNotFoundException e)
			{
				mv.setViewName("redirect:/");
				List<String> errors = new ArrayList<>();
				errors.add("Error: The form request to edit this role is invalid, please try again");
				mv.addObject("errors", errors);
			}
		}
		
		return mv;
	}
	
	@RequestMapping(value = "/roleManagement", method = RequestMethod.POST)
	public ModelAndView roleManagement(Role role)
	{
		// TODO: This might need to be a redirect to /role?rid{id} - test what happens if user refresh the page after creating
		ModelAndView mv = new ModelAndView("roleDetails");
		
		if (role.getRoleId() == null)
		{
			// Request to create a new role
			try
			{
				Response response = ClientBuilder.newClient()
												 .target(links.getBaseLink("base_role"))
												 .request()
												 .header("Authorization", "Bearer " + session.getToken())
												 .post(Entity.json(role));
				
				if (response.getStatus() == HttpStatus.CREATED.value())
				{
					Role createdRole = response.readEntity(Role.class);
					mv.addObject("role", createdRole);
					mv.addObject("success", "The role has been created successfully");
				}
				else
				{
					if (response.getHeaderString("comsAPI") != null)
					{
						ErrorMessages errors = response.readEntity(ErrorMessages.class);
						mv.setViewName("redirect:/roles");
						mv.addObject("errors", errors.getErrors());
						mv.addObject("messages", errors.getMessages());
					}
					else
					{
						List<String> errors = new ArrayList<>();
						errors.add("Unknown error");
						errors.add("Error code: " + response.getStatus());
						mv.setViewName("redirect:/roles");
						mv.addObject("errors", errors);
					}
				}
			}
			catch (LinkNotFoundException e)
			{
				mv.setViewName("redirect:/");
				List<String> errors = new ArrayList<>();
				errors.add("Error: The request to create a new role is invalid, please try again");
				mv.addObject("errors", errors);
			}
		}
		else 
		{
			// Request to update an existing role
			
			try
			{
				Response response = ClientBuilder.newClient()
						 .target(links.getBaseLink("base_role"))
						 .request()
						 .header("Authorization", "Bearer " + session.getToken())
						 .put(Entity.json(role));
				
				if (response.getStatus() == HttpStatus.OK.value())
				{
					Role createdRole = response.readEntity(Role.class);
					mv.addObject("role", createdRole);
					mv.addObject("success", "The role has been updated successfully");
				}
				else
				{
					if (response.getHeaderString("comsAPI") != null)
					{
						ErrorMessages errors = response.readEntity(ErrorMessages.class);
						mv.setViewName("redirect:/roles");
						mv.addObject("errors", errors.getErrors());
						mv.addObject("messages", errors.getMessages());
					}
					else
					{
						List<String> errors = new ArrayList<>();
						errors.add("Unknown error");
						errors.add("Error code: " + response.getStatus());
						mv.setViewName("redirect:/roles");
						mv.addObject("errors", errors);
					}
				}
			}
			catch (LinkNotFoundException e)
			{
				mv.setViewName("redirect:/");
				List<String> errors = new ArrayList<>();
				errors.add("Error: The request to update an existing role is invalid, please try again");
				mv.addObject("errors", errors);
			}
		}
		
		return mv;
	}
	
	@RequestMapping(value = "/deleteRole", method = RequestMethod.POST)
	public ModelAndView deleteRole(@RequestParam Integer roleId)
	{
		ModelAndView mv = new ModelAndView("redirect:/roles");
		
		try
		{
			Response response = ClientBuilder.newClient()
											 .target(links.getBaseLink("base_role") + "/" + roleId)
											 .request()
											 .header("Authorization", "Bearer " + session.getToken())
											 .delete();
			
			if (response.getStatus() ==  HttpStatus.NO_CONTENT.value())
			{
				mv.addObject("success", "The role has been deleted successfully");
			}
			else
			{				
				if (response.getHeaderString("comsAPI") != null)
				{
					ErrorMessages errors = response.readEntity(ErrorMessages.class);
					mv.setViewName("redirect:/roles");
					mv.addObject("errors", errors.getErrors());
					mv.addObject("messages", errors.getMessages());
				}
				else
				{
					List<String> errors = new ArrayList<>();
					errors.add("Unknown error");
					errors.add("Error code: " + response.getStatus());					
					mv.setViewName("redirect:/roles");
					mv.addObject("errors", errors);
				}
			}
		}
		catch (LinkNotFoundException e)
		{
			mv.setViewName("redirect:/");
			List<String> errors = new ArrayList<>();
			errors.add("Error: The request to delete a role is invalid, please try again");
			mv.addObject("errors", errors);
		}
		return mv;
	}
	
	@RequestMapping(value = "/removeRoleMember", method = RequestMethod.POST)
	public ModelAndView removeRoleMembers(@RequestParam int roleId,
										  @RequestParam(required = false) Integer[] userId)
	{
		System.out.println("=========> RoleId received: " + roleId);
		if (userId != null)
		{
			// TODO: Remove the users received from the role members
			
			for (int i = 0; i < userId.length; i++)
			{
				System.out.println("==========> User ID found - index: " + i + " - id: " + userId[i]);
			} 
		}
		
		// TODO: Redirect the user to the role details
		return null;
	}
	
	@RequestMapping(value = "/addRoleMember", method = RequestMethod.POST)
	public ModelAndView addRoleMembers(@RequestParam int roleId,
									   @RequestParam(required = false) Integer[] userId)
	{
		System.out.println("=========> RoleId received: " + roleId);
		if (userId != null)
		{
			// TODO: Add the users received to the role members
			
			for (int i = 0; i < userId.length; i++)
			{
				System.out.println("==========> User ID found - index: " + i + " - id: " + userId[i]);
			} 
		}
		
		// TODO: Redirect the user to the role details
		return null;
	}
}

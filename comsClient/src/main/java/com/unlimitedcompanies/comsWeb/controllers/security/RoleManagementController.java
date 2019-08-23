package com.unlimitedcompanies.comsWeb.controllers.security;

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
import com.unlimitedcompanies.comsWeb.representations.security.ErrorMessages;
import com.unlimitedcompanies.comsWeb.representations.security.Role;

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
		ModelAndView mv = new ModelAndView("redirect:/role");
		mv.addObject("rid", roleId);
		
		if (userId != null)
		{
			try
			{
				Response removeMemberResponse = ClientBuilder.newClient()
												 .target(links.getBaseLink("base_role") + "/" + roleId + "/nonmembers")
												 .request()
												 .header("Authorization", "Bearer " + session.getToken())
												 .put(Entity.json(userId));
				
				if (removeMemberResponse.getStatus() == HttpStatus.NO_CONTENT.value())
				{
					mv.addObject("success", "The members have been successfully removed from the role");
				}
				else
				{
					if (removeMemberResponse.getHeaderString("comsAPI") != null)
					{
						ErrorMessages errors = removeMemberResponse.readEntity(ErrorMessages.class);
						mv.setViewName("redirect:/roles");
						mv.addObject("errors", errors.getErrors());
						mv.addObject("messages", errors.getMessages());
					}
					else
					{
						List<String> errors = new ArrayList<>();
						errors.add("Unknown error");
						errors.add("Error code: " + removeMemberResponse.getStatus());
						mv.setViewName("redirect:/roles");
						mv.addObject("errors", errors);
					}
				}
			}
			catch (LinkNotFoundException e)
			{
				mv.setViewName("redirect:/roles");
				List<String> errors = new ArrayList<>();
				errors.add("Error: The request to remove a member from the role is invalid, please try again");
				mv.addObject("errors", errors);
			}
		}
		
		return mv;
	}
	
	@RequestMapping(value = "/addRoleMember", method = RequestMethod.POST)
	public ModelAndView addRoleMembers(@RequestParam int roleId,
									   @RequestParam(required = false) Integer[] userId)
	{
		ModelAndView mv = new ModelAndView("redirect:/role");
		mv.addObject("rid", roleId);
		
		if (userId != null)
		{
			try
			{
				Response addMemberResponse = ClientBuilder.newClient()
												 .target(links.getBaseLink("base_role") + "/" + roleId + "/members")
												 .request()
												 .header("Authorization", "Bearer " + session.getToken())
												 .put(Entity.json(userId));
				
				if (addMemberResponse.getStatus() == HttpStatus.NO_CONTENT.value())
				{
					mv.addObject("success", "The users have been successfully added to the role");
				}
				else
				{
					if (addMemberResponse.getHeaderString("comsAPI") != null)
					{
						ErrorMessages errors = addMemberResponse.readEntity(ErrorMessages.class);
						mv.setViewName("redirect:/roles");
						mv.addObject("errors", errors.getErrors());
						mv.addObject("messages", errors.getMessages());
					}
					else
					{
						List<String> errors = new ArrayList<>();
						errors.add("Unknown error");
						errors.add("Error code: " + addMemberResponse.getStatus());
						mv.setViewName("redirect:/roles");
						mv.addObject("errors", errors);
					}
				}
			}
			catch (LinkNotFoundException e)
			{
				mv.setViewName("redirect:/roles");
				List<String> errors = new ArrayList<>();
				errors.add("Error: The request to add a user to the role is invalid, please try again");
				mv.addObject("errors", errors);
			}
		}
		
		return mv;
	}
}

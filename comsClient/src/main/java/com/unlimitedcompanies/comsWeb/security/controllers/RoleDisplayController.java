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
import com.unlimitedcompanies.comsWeb.security.representations.ErrorMessages;
import com.unlimitedcompanies.comsWeb.security.representations.Role;
import com.unlimitedcompanies.comsWeb.security.representations.RoleCollectionResponse;
import com.unlimitedcompanies.comsWeb.security.representations.UserDetailedCollection;

@Controller
public class RoleDisplayController
{
	@Autowired
	UserSessionManager session;
	
	@Autowired
	LinkManager links;
	
	@RequestMapping(value = "/roles")
	public ModelAndView displayAllRoles(HttpServletRequest request,
										@RequestParam(name = "errors", required = false) List<String> errors,
										@RequestParam(name = "messages", required = false) List<String> messages,
										@RequestParam(name = "success", required = false) String success,
										@RequestParam(name = "pag", required = false) Integer pag,
										@RequestParam(name = "epp", required = false) Integer epp)
	{
		ModelAndView mv = new ModelAndView("roleList");
		
		if (errors == null) errors = new ArrayList<>();
		if (messages == null) messages = new ArrayList<>();
		
		String url = null;
		try
		{
			url = links.getBaseLink("base_role") + "s";
		}
		catch (LinkNotFoundException e)
		{
			mv.setViewName("redirect:/");
			List<String> errorList = new ArrayList<>();
			if (errors != null && !errors.isEmpty()) errorList.addAll(errors);
			errorList.add("Error: The request to display the roles is invalid");
			mv.addObject("errors", errorList);
			
			return mv;
		}
		
		if (pag != null)
		{
			url += "?pag=" + pag;
			if (epp != null) url += "&epp=" + epp;
		}
		
		Response response = ClientBuilder.newClient()
										 .target(url)
										 .request()
										 .header("Authorization", "Bearer " + session.getToken())
										 .get();
		
		if (response.getStatus() == HttpStatus.OK.value())
		{
			RoleCollectionResponse roles = response.readEntity(RoleCollectionResponse.class);
			mv.addObject("roles", roles.getRoles());
			
			if (roles.getPrev() != null)
			{
				mv.addObject("previous", request.getContextPath() + "/roles?pag=" + roles.getPrev());
			}
			
			if (roles.getNext() != null)
			{
				mv.addObject("next", request.getContextPath() + "/roles?pag=" + roles.getNext());
			}
		}
		else 
		{
			if (response.getHeaderString("comsAPI") != null)
			{
				ErrorMessages errorList = response.readEntity(ErrorMessages.class);
				mv.setViewName("redirect:/");
				errors.addAll(errorList.getErrors());
				messages.addAll(errorList.getMessages());
			}
			else
			{
				errors.add("Unknown error");
				errors.add("Error code: " + response.getStatus());
			}
		}
		
		if (!errors.isEmpty()) mv.addObject("errors", errors);
		if (!messages.isEmpty()) mv.addObject("messages", messages);
		if (success != null) mv.addObject("success", success);
		
		mv.addObject("loggedUser", session.getLogedUserFullName());
		
		return mv;
	}
	
	@RequestMapping(value = "/role", method = RequestMethod.GET)
	public ModelAndView searchUserDetails(@RequestParam(name = "rid") Integer id,
										  @RequestParam(name = "errors", required = false) List<String> errors,
										  @RequestParam(name = "success", required = false) String success)
	{
		ModelAndView mv = new ModelAndView("roleDetails");
		
		if (errors == null)
		{
			errors = new ArrayList<>();
		}
		
		try
		{
			Response roleResponse = ClientBuilder.newClient()
											 .target(links.getBaseLink("base_role") + "/" + id)
											 .request()
											 .header("Authorization", "Bearer " + session.getToken())
											 .get();
			
			if (roleResponse.getStatus() == HttpStatus.OK.value())
			{
				Role roleResult = roleResponse.readEntity(Role.class);
				mv.addObject("role", roleResult);
				
				if (success != null) mv.addObject("success", success);
				
				// Request a list of user members of the role
				Response roleMembersResponse = ClientBuilder.newClient()
												 .target(roleResult.getLink("role_members").getHref())
												 .request()
												 .header("Authorization", "Bearer " + session.getToken())
												 .get();
				
				if (roleMembersResponse.getStatus() == HttpStatus.OK.value())
				{
					UserDetailedCollection members = roleMembersResponse.readEntity(UserDetailedCollection.class);
					mv.addObject("members", members.getUsers());
					
					// TODO: Fix the hard coded url strings					
					mv.addObject("role_removemember", "http://localhost:8080/coms/removeRoleMember");
					mv.addObject("role_addmember", "http://localhost:8080/coms/addRoleMember");
				}
				
				//Request a list of users non-members of the role
				Response roleNonMembersResponse = ClientBuilder.newClient()
													 .target(roleResult.getLink("role_nonMembers").getHref())
													 .request()
													 .header("Authorization", "Bearer " + session.getToken())
													 .get();
				
				if (roleNonMembersResponse.getStatus() == HttpStatus.OK.value())
				{
					UserDetailedCollection nonMembers = roleNonMembersResponse.readEntity(UserDetailedCollection.class);
					mv.addObject("nonMembers", nonMembers.getUsers());
				}
			}
			else
			{
				mv.setViewName("redirect:/roles");
				if (roleResponse.getHeaderString("comsAPI") != null)
				{
					ErrorMessages errorList = roleResponse.readEntity(ErrorMessages.class);
					errors.addAll(errorList.getErrors());
					mv.addObject("messages", errorList.getMessages());
				}
				else
				{
					List<String> errorList = new ArrayList<>();
					errorList.add("Unknown error");
					errorList.add("Error code: " + roleResponse.getStatus());
					errors.addAll(errorList);
				}
			}
		}
		catch (LinkNotFoundException e)
		{
			mv.setViewName("redirect:/roles");
			List<String> errorList = new ArrayList<>();
			errorList.add("Error: The request to display a user is invalid");
			errors.addAll(errorList);
		}
		
		mv.addObject("loggedUser", session.getLogedUserFullName());
		mv.addObject("errors", errors);
		return mv;
	}
}

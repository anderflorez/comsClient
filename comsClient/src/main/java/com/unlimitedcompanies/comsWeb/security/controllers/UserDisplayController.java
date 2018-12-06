package com.unlimitedcompanies.comsWeb.security.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.unlimitedcompanies.comsWeb.appManagement.LinkManager;
import com.unlimitedcompanies.comsWeb.appManagement.UserSessionManager;
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
		
		String url = "http://localhost:8080/comsws/rest/users";
		
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
		
		links.addBaseLink("user", users.getLink("base_url").getHref());
		
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
}

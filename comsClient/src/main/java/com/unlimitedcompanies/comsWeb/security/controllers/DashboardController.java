package com.unlimitedcompanies.comsWeb.security.controllers;

import java.util.ArrayList;
import java.util.List;

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
import com.unlimitedcompanies.comsWeb.appManagement.UserSessionManager;
import com.unlimitedcompanies.comsWeb.security.representations.ResourceLink;
import com.unlimitedcompanies.comsWeb.security.representations.ResourceLinkCollection;

@Controller
public class DashboardController
{
	@Autowired
	LinkManager links;
	
	@Autowired
	UserSessionManager session;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView showDashboard(@RequestParam(name = "errors", required = false) List<String> errors)
	{
		ModelAndView mv = new ModelAndView("dashboard");
		mv.addObject("loggedUser", session.getLogedUserFullName());
		
		Response response = ClientBuilder.newClient()
								.target("http://localhost:8080/comsws/rest/directory")
								.request()
								.header("Authorization", "Bearer " + session.getToken())
								.get();
		
		if (response.getStatus() == HttpStatus.OK.value())
		{
			ResourceLinkCollection resourceResponse = response.readEntity(ResourceLinkCollection.class);
			List<ResourceLink> resources = resourceResponse.getResources();
			for (ResourceLink next : resources)
			{
				links.addBaseLink(next.getResourceName(), next.getResourceBaseURL());
			}
			if (errors != null && !errors.isEmpty())
			{
				mv.addObject("errors", errors);
			}
		}
		else
		{
			List<String> errorList = new ArrayList<>();
			if (errors != null && !errors.isEmpty()) errorList.addAll(errors);
			errorList.add("Error: The API URIs list could not be retrieved");
			mv.addObject("errors", errorList);
		}
		
		return mv;
	}
}

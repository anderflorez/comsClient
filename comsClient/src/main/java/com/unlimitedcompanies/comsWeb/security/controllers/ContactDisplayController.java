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
import com.unlimitedcompanies.comsWeb.security.representations.ContactCollection;

@Controller
public class ContactDisplayController
{
	@Autowired
	UserSessionManager session;
	
	@Autowired
	LinkManager links;
	
	@RequestMapping(value = "/contacts", method = RequestMethod.GET)
	public ModelAndView findAllContacts(HttpServletRequest request,
										@RequestParam(name = "errors", required = false) List<String> errors,
										@RequestParam(name = "success", required = false) String success,
										@RequestParam(name = "pag", required = false) Integer pag,
										@RequestParam(name = "epp", required = false) Integer epp)
	{
		ModelAndView mv = new ModelAndView("contactList");
		
		String url = "http://localhost:8080/comsws/rest/contacts";
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
		
		ContactCollection contactResponse = response.readEntity(ContactCollection.class);
		
		links.addBaseLink("contact", contactResponse.getLink("base_url").getHref());
		
		mv.addObject("contacts", contactResponse.getContacts());
		
		if (errors != null)
		{
			mv.addObject("errors", errors);
		}
		
		if (success != null)
		{
			mv.addObject("success", success);
		}
		
		if (contactResponse.getPrevPage() != null)
		{
			mv.addObject("previous", request.getContextPath() + "/contacts?pag=" + contactResponse.getPrevPage());
		}
		
		if (contactResponse.getNextPage() != null)
		{
			mv.addObject("next", request.getContextPath() + "/contacts?pag=" + contactResponse.getNextPage());
		}
		
		mv.addObject("loggedUser", session.getLogedUserFullName());
		return mv;
	}
	
	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public ModelAndView findContactDetails(@RequestParam("cid") Integer id)
	{
		ModelAndView mv = new ModelAndView("contactDetails");
		
		try
		{
			Response response = ClientBuilder.newClient()
											 .target(links.getBaseLink("contact") + id)
											 .request()
											 .header("Authorization", "Bearer " + session.getToken())
											 .get();

			ContactCollection contactResponse = response.readEntity(ContactCollection.class);
			
			if (contactResponse.getStatusCode() == HttpStatus.NOT_FOUND.value())
			{
				mv.setViewName("redirect:/contacts");
				mv.addObject("errors", contactResponse.getErrors());
			}
			else if (!contactResponse.getContacts().isEmpty())
			{
				mv.addObject("contact", contactResponse.getContacts().get(0));
				mv.addObject("loggedUser", session.getLogedUserFullName());
			}
			else 
			{
				mv.setViewName("redirect:/contacts");
				List<String> errors = new ArrayList<>();
				errors.add("Unknown error " + response.getStatus());
				mv.addObject("errors", errors);
			}
		} 
		catch (LinkNotFoundException e)
		{
			mv.setViewName("redirect:/contacts");
			mv.addObject("error", "Error: The request to display a contact is invalid");
		}		
		
		return mv;

	}
}

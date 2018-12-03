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
	public ModelAndView findAllContacts(@RequestParam(name = "errors", required = false) List<String> errors,
										@RequestParam(name = "success", required = false) String success,
										@RequestParam(name = "pag", required = false) Integer pag,
										@RequestParam(name = "epp", required = false) Integer epp)
	{
		ModelAndView mv = new ModelAndView("contactList");
		
		String url;
		if (pag != null && epp != null)
		{
			url = "http://localhost:8080/comsws/rest/contacts?pag=" + pag + "&epp=" + epp;
		}
		else
		{
			url = "http://localhost:8080/comsws/rest/contacts";
		}
		
		// TODO: Improve the address hard coding if possible
		Response response = ClientBuilder.newClient()
										 .target(url)
										 .request()
										 .header("Authorization", "Bearer " + session.getToken())
										 .get();
		
		ContactCollection contactsResponse = response.readEntity(ContactCollection.class);
		
		links.addBaseLink("contact", contactsResponse.getLink("base_url").getHref());
		
		mv.addObject("contacts", contactsResponse.getContacts());
		if (errors != null)
		{
			mv.addObject("errors", errors);
		}
		if (success != null)
		{
			mv.addObject("success", success);
		}
		
		mv.addObject("previous", contactsResponse.getLink("previous").getHref());
		mv.addObject("next", contactsResponse.getLink("next").getHref());
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

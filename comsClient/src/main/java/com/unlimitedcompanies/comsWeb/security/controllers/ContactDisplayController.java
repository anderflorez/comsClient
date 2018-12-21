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
import com.unlimitedcompanies.comsWeb.security.representations.ContactCollection;
import com.unlimitedcompanies.comsWeb.security.representations.ErrorMessages;

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
		
		String url = null;
		try
		{
			url = links.getBaseLink("base_contact") + "s";
		}
		catch (LinkNotFoundException e)
		{
			mv.setViewName("redirect:/");
			List<String> errorList = new ArrayList<>();
			if (errors != null && !errors.isEmpty()) errorList.addAll(errors);
			errorList.add("Error: The request to display a contact is invalid");
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
		
		ContactCollection contactResponse = response.readEntity(ContactCollection.class);
		
		mv.addObject("loggedUser", session.getLogedUserFullName());
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
		
		return mv;
	}
	
	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public ModelAndView findContactDetails(@RequestParam("cid") Integer id)
	{
		ModelAndView mv = new ModelAndView("contactDetails");
		
		try
		{
			Response response = ClientBuilder.newClient()
											 .target(links.getBaseLink("base_contact") + "/" + id)
											 .request()
											 .header("Authorization", "Bearer " + session.getToken())
											 .get();
			
			if (response.getStatus() == HttpStatus.OK.value())
			{
				Contact contact = response.readEntity(Contact.class);
				mv.addObject("contact", contact);
				mv.addObject("loggedUser", session.getLogedUserFullName());		
			}
			else
			{
				ErrorMessages error = response.readEntity(ErrorMessages.class);
				mv.setViewName("redirect:/contacts");
				mv.addObject("errors", error.getErrors());
				mv.addObject("messages", error.getMessages());
			}
		} 
		catch (LinkNotFoundException e)
		{
			mv.setViewName("redirect:/contacts");
			List<String> errors = new ArrayList<>();
			errors.add("Error: The request to display a contact is invalid");
			mv.addObject("errors", errors);
		}
		
		return mv;

	}
}

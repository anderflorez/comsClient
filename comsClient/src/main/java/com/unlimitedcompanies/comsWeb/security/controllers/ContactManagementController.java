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
import com.unlimitedcompanies.comsWeb.security.representations.Contact;
import com.unlimitedcompanies.comsWeb.security.representations.ContactCollection;

@Controller
public class ContactManagementController
{
	@Autowired
	UserSessionManager session;
	
	@Autowired
	LinkManager links;
	
	@RequestMapping(value = "/contactManagement", method = RequestMethod.GET)
	public ModelAndView displayContactForm(@RequestParam(name = "cid", required = false) Integer cId)
	{	
		ModelAndView mv = new ModelAndView("contactForm");

		if (cId == null)
		{
			// Display the form to create a new contact
			mv.addObject("contact", new Contact());
		}
		else
		{
			// Display the form to edit the existing contact
			try
			{
				Response response = ClientBuilder.newClient()
												 .target(links.getBaseLink("contact") + cId)
												 .request()
												 .header("Authorization", "Bearer " + session.getToken())
												 .get();
												 
				ContactCollection contactResponse = response.readEntity(ContactCollection.class);
				
				if (contactResponse.getStatusCode() == HttpStatus.OK.value() && !contactResponse.getContacts().isEmpty())
				{
					Contact contact = contactResponse.getContacts().get(0);
					mv.addObject("contact", contact);
				}
				else
				{
					mv.setViewName("redirect:/contacts");
					mv.addObject("errors", contactResponse.getErrors());
				}
			} 
			catch (LinkNotFoundException e)
			{
				mv.setViewName("redirect:/contacts");
				List<String> errors = new ArrayList<>();
				errors.add("Error: The request to edit the contact is invalid, please try again");
				mv.addObject("errors", errors);
			}
		}
		
		mv.addObject("loggedUser", session.getLogedUserFullName());
		return mv;
	}
	
	@RequestMapping(value = "/contactManagement", method = RequestMethod.POST)
	public ModelAndView createContact(Contact contact)
	{	
		ModelAndView mv = new ModelAndView("contactDetails");
		
		if (contact.getContactId() == null)
		{
			// Make a request to create a new contact			
			try
			{
				Response response = ClientBuilder.newClient()
												 .target(links.getBaseLink("contact"))
												 .request()
												 .header("Authorization", "Bearer " + session.getToken())
												 .post(Entity.json(contact));
				
				ContactCollection contactResponse = response.readEntity(ContactCollection.class);
				
				if (response.getStatus() == HttpStatus.CREATED.value() && !contactResponse.getContacts().isEmpty())
				{
					mv.addObject("contact", contactResponse.getContacts().get(0));
					mv.addObject("success", contactResponse.getSuccess());
				}
				else 
				{
					mv.setViewName("contactForm");
					mv.addObject("contact", contact);
					mv.addObject("errors", contactResponse.getErrors());
				}
			} 
			catch (LinkNotFoundException e)
			{
				mv.setViewName("redirect:/contacts");
				List<String> errors = new ArrayList<>();
				errors.add("An internal error has occurred, please try again or contact your system administrator");
				mv.addObject("errors", errors);
				return mv;
			}
			
		}
		else
		{
			// Make a request to update an existing contact
			try
			{
				Response response = ClientBuilder.newClient()
												 .target(links.getBaseLink("contact"))
												 .request()
												 .header("Authorization", "Bearer " + session.getToken())
												 .put(Entity.json(contact));
				
				ContactCollection contactResponse = response.readEntity(ContactCollection.class);
				
				if (response.getStatus() == HttpStatus.OK.value() && !contactResponse.getContacts().isEmpty())
				{
					mv.addObject("contact", contactResponse.getContacts().get(0));
					mv.addObject("success", contactResponse.getSuccess());
				}
				else 
				{
					mv.setViewName("contactForm");
					mv.addObject("contact", contact);
					mv.addObject("errors", contactResponse.getErrors());
				}		
			} 
			catch (LinkNotFoundException e)
			{
				mv.setViewName("redirect:/contacts");
				List<String> errors = new ArrayList<>();
				errors.add("An internal error has occurred, please try again or contact your system administrator");
				mv.addObject("errors", errors);
				return mv;
			}
		}
		
		mv.addObject("loggedUser", session.getLogedUserFullName());
		return mv;
	}
	
	@RequestMapping(value = "/deleteContact", method = RequestMethod.POST)
	public ModelAndView deleteContact(@RequestParam Integer contactId)
	{
		ModelAndView mv = new ModelAndView("redirect:/contacts");
		try
		{
			Response response = ClientBuilder.newClient()
											 .target(links.getBaseLink("contact") + contactId)
											 .request()
											 .header("Authorization", "Bearer " + session.getToken())
											 .delete();
			
			ContactCollection contactResponse = response.readEntity(ContactCollection.class);

			if (response.getStatus() == HttpStatus.OK.value())
			{
				mv.addObject("contact", new Contact());
				mv.addObject("success", contactResponse.getSuccess());
			}
			else
			{
				mv.setViewName("redirect:/contacts");
				mv.addObject("errors", contactResponse.getErrors());
			}
		} 
		catch (LinkNotFoundException e)
		{
			mv.setViewName("redirect:/contacts");
			List<String> errors = new ArrayList<>();
			errors.add("An internal error has occurred, please try again or contact your system administrator");
			mv.addObject("errors", errors);
			return mv;
		}
		
		return mv;
	}
}

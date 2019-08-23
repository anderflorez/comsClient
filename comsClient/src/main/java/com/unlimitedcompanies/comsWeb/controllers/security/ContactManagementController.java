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
import com.unlimitedcompanies.comsWeb.representations.security.Contact;
import com.unlimitedcompanies.comsWeb.representations.security.ErrorMessages;

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
												 .target(links.getBaseLink("base_contact") + "/" + cId)
												 .request()
												 .header("Authorization", "Bearer " + session.getToken())
												 .get();
				
				if (response.getStatus() == HttpStatus.OK.value())
				{
					Contact contact = response.readEntity(Contact.class);
					mv.addObject("contact", contact);					
				}
				else
				{
					// TODO: Change this to cover all the options - provider server known errors and unknown errors with http status - see user and role management classes
					ErrorMessages errors = response.readEntity(ErrorMessages.class);
					mv.setViewName("redirect:/contacts");
					mv.addObject("errors", errors.getErrors());
					mv.addObject("messages", errors.getMessages());
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
												 .target(links.getBaseLink("base_contact"))
												 .request()
												 .header("Authorization", "Bearer " + session.getToken())
												 .post(Entity.json(contact));
				
				if (response.getStatus() == HttpStatus.CREATED.value())
				{
					Contact contactResponse = response.readEntity(Contact.class);
					mv.addObject("contact", contactResponse);
					mv.addObject("success", "The client has been created successfully");
				}
				else 
				{
					// TODO: Change this to cover all the options - provider server known errors and unknown errors with http status - see user and role management classes
					ErrorMessages errors = response.readEntity(ErrorMessages.class);
					mv.setViewName("contactForm");
					mv.addObject("errors", errors.getErrors());
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
												 .target(links.getBaseLink("base_contact"))
												 .request()
												 .header("Authorization", "Bearer " + session.getToken())
												 .put(Entity.json(contact));
				
				if (response.getStatus() == HttpStatus.OK.value())
				{
					Contact updatedContact = response.readEntity(Contact.class);
					mv.addObject("contact", updatedContact);
					mv.addObject("success", "The contact has been updated successfully");					
				}
				else 
				{
					// TODO: Change this to cover all the options - provider server known errors and unknown errors with http status - see user and role management classes
					ErrorMessages errors = response.readEntity(ErrorMessages.class);
					mv.setViewName("contactForm");
					mv.addObject("contact", contact);
					mv.addObject("errors", errors.getErrors());
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
											 .target(links.getBaseLink("base_contact") + "/" + contactId)
											 .request()
											 .header("Authorization", "Bearer " + session.getToken())
											 .delete();
			
			if (response.getStatus() == HttpStatus.NO_CONTENT.value())
			{
				mv.addObject("success", "The contact has been deleted successfully");				
			}
			else
			{
				// TODO: Change this to cover all the options - provider server known errors and unknown errors with http status - see user and role management classes
				ErrorMessages errors = response.readEntity(ErrorMessages.class);
				mv.setViewName("redirect:/contacts");
				mv.addObject("errors", errors.getErrors());
				mv.addObject("messages", errors.getMessages());
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

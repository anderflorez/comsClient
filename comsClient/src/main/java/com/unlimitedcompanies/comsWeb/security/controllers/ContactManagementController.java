package com.unlimitedcompanies.comsWeb.security.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.unlimitedcompanies.comsWeb.appManagement.LinkManager;
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

	private RestTemplate template;
	
	public ContactManagementController()
	{
		this.template  = new RestTemplate();
		template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
	}
	
	@RequestMapping(value = "/contacts", method = RequestMethod.GET)
	public ModelAndView findAllContacts(@RequestParam(name = "error", required = false) String error)
	{
		// TODO: Improve the address hard coding if possible
		String contactsUrl = "http://localhost:8080/comsws/rest/contacts";
		
		ModelAndView mv = new ModelAndView("contactList");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + session.getToken());
		HttpEntity<ContactCollection> request = new HttpEntity<>(headers);
		
		ResponseEntity<ContactCollection> response = template.exchange(contactsUrl, HttpMethod.GET, request, ContactCollection.class);
		
		ContactCollection contactCollection = response.getBody();
		for (Contact contact : contactCollection.getContacts())
		{
			links.addLink("contacts", contact.getContactId(), contact.getLink("self").getHref());
		}
		
		mv.addObject("loggedUser", session.getLogedUserFullName());
		mv.addObject("contacts", response.getBody().getContacts());
		if (error != null)
		{
			mv.addObject("error", error);
		}
		
		return mv;
	}
	
	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public ModelAndView findContactDetails(@RequestParam("cid") Integer id,
										   HttpServletResponse httpResponse)
	{
		ModelAndView mv = new ModelAndView("contactDetails");
		
		String url = links.getLink("contacts", id);
		url = "http://localhost:8080/comsws/rest/contact/2";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + session.getToken());
		HttpEntity<ContactCollection> request = new HttpEntity<>(headers);
		
		ResponseEntity<ContactCollection> response = null;
		try
		{
			response = template.exchange(url, HttpMethod.GET, request, ContactCollection.class);
			System.out.println("============> Status code: " + response.getStatusCode());
		} 
		catch (ResourceAccessException e)
		{
			mv.setViewName("redirect:/contacts");
			mv.addObject("error", "Error: The requested contact could not be found");
			return mv;
		}
		catch (HttpClientErrorException e)
		{
			System.out.println("================> Http Status: " + response.getStatusCode());
			if (httpResponse.getStatus() == 404)
			{
				System.out.println("===========\n\nHttp Status 404 found\n\n===========");
			}
			mv.setViewName("redirect:/contacts");
			mv.addObject("error", "Error: The requested contact could not be found");
			return mv;
		}
		
//		if (response.getStatusCode().equals(HttpStatus.NOT_FOUND))
//		{
//			System.out.println("===========\n\nHttp Status 404 catched\n\n===========");
//			mv.setViewName("redirect:/contacts");
//			mv.addObject("error", "Error: The requested contact could not be found");
//		}
//		else
//		{
//			// TODO: Search if there is a user related to the contact - if no user then a new user option can be displayed
//			
//			mv.addObject("contact", response.getBody().getContacts().get(0));
//			mv.addObject("loggedUser", session.getLogedUserFullName());
//		}		
		
		return mv;
	}
}

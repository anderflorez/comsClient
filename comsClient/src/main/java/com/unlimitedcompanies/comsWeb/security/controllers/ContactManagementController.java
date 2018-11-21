package com.unlimitedcompanies.comsWeb.security.controllers;

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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.unlimitedcompanies.comsWeb.config.UserSessionManager;
import com.unlimitedcompanies.comsWeb.security.representations.Contact;
import com.unlimitedcompanies.comsWeb.security.representations.ContactCollection;

@Controller
public class ContactManagementController
{
	@Autowired
	UserSessionManager session;

	private String contactsUrl;
	private RestTemplate template;
	
	public ContactManagementController()
	{
		this.contactsUrl = "http://localhost:8080/comsws/rest/contacts";
		this.template  = new RestTemplate();
		template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
	}
	
	@RequestMapping(value = "/contacts", method = RequestMethod.GET)
	public ModelAndView findAllContacts(@RequestParam(name = "error", required = false) String error)
	{
		ModelAndView mv = new ModelAndView("/pages/security/contactView.jsp");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + session.getToken());
		HttpEntity<ContactCollection> request = new HttpEntity<>(headers);
		
		ResponseEntity<ContactCollection> response = template.exchange(contactsUrl, HttpMethod.GET, request, ContactCollection.class);
		
		mv.addObject("loggedUser", session.getLogedUserFullName());
		mv.addObject("contacts", response.getBody().getContacts());
		if (error != null)
		{
			mv.addObject("error", error);
		}
		
		return mv;
	}
	
	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public ModelAndView findContactDetails(@RequestParam("cid") String id)
	{
		ModelAndView mv = new ModelAndView("/pages/security/contactDetails.jsp");
		
		String url = "http://localhost:8080/comsws/rest/contact/" + id;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + session.getToken());
		HttpEntity<ContactCollection> request = new HttpEntity<>(headers);
		
		ResponseEntity<Contact> response = null;
		try
		{
			response = template.exchange(url, HttpMethod.GET, request, Contact.class);
		} 
		catch (RestClientException e)
		{

		}
		
		if (response.getStatusCode().equals(HttpStatus.NOT_FOUND))
		{
			System.out.println("===========\n\nHttp Status 404 catched\n\n===========");
			mv.setViewName("/contacts");
			mv.addObject("error", "Error: The requested contact could not be found");
		}
		else
		{
			// TODO: Search if there is a user related to the contact
			
			mv.addObject("contact", response.getBody());
			mv.addObject("loggedUser", session.getLogedUserFullName());			
		}		
		
		return mv;
	}
}

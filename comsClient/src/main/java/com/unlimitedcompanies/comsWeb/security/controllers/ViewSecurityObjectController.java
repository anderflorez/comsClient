package com.unlimitedcompanies.comsWeb.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.unlimitedcompanies.comsWeb.config.UserSessionManager;
import com.unlimitedcompanies.comsWeb.security.representations.ContactCollection;

@Controller
public class ViewSecurityObjectController
{
	@Autowired
	UserSessionManager session;
	
	@RequestMapping(value = "/contacts", method = RequestMethod.GET)
	public ModelAndView showContacts(@RequestParam(name = "error", required = false) String error)
	{
		ModelAndView mv = new ModelAndView("/pages/security/contactView.jsp");
		String token = session.getToken();
		RestTemplate template = new RestTemplate();
		template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		String url = "http://localhost:8080/comsws/rest/contacts";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		HttpEntity<ContactCollection> request = new HttpEntity<>(headers);
		ResponseEntity<ContactCollection> contacts = template.exchange(url, HttpMethod.GET, request, ContactCollection.class);
		
		mv.addObject("loggedUser", session.getLogedUserFullName());
		mv.addObject("contacts", contacts.getBody().getContacts());
		if (error != null)
		{
			mv.addObject("error", error);
		}
		
		return mv;
	}
}

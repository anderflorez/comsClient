package com.unlimitedcompanies.comsWeb.security.controllers;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unlimitedcompanies.comsWeb.appManagement.UserSessionManager;
import com.unlimitedcompanies.comsWeb.security.representations.Contact;
import com.unlimitedcompanies.comsWeb.security.representations.User;

@Controller
public class SessionAndTokenManager
{
	@Autowired
	UserSessionManager session;
	
	@RequestMapping("/tokenmanager")
	public ModelAndView requestToken(@RequestParam("code") String code,
									 HttpServletRequest httpRequest) throws IOException
	{
		// Convert the next request to use Jersey instead of the Spring RestTemplate
		
		// Request the access token and store it in the session
		RestTemplate template = new RestTemplate();
		template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		
		String url = "http://localhost:8080/comsws/oauth/token";

		String credentials = "comsClient:somesecret";
		String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
		MultiValueMap<String, String> tokenRequestHeaders = new LinkedMultiValueMap<>();
		tokenRequestHeaders.add("Authorization", "Basic " + encodedCredentials);
		
		MultiValueMap<String, String> tokenRequestBody = new LinkedMultiValueMap<>();
		tokenRequestBody.add("code", code);
		tokenRequestBody.add("grant_type", "authorization_code");
		tokenRequestBody.add("redirect_uri", "http://localhost:8080/coms/tokenmanager");
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(tokenRequestBody, tokenRequestHeaders);
		
		ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, request, String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(response.getBody());
		String token = node.path("access_token").asText();
		
		session.setToken(token);
		
		// TODO: Delete the next printing lines
		System.out.println("=====================================\n\n");
		System.out.println("The obtained token is: " + session.getToken());
		System.out.println("\n\n=====================================");
		

		// Obtain the information about the current logged user and save it in the session
		Response loggedUserResponse = ClientBuilder.newClient()
										 .target("http://localhost:8080/comsws/rest/loggedUser")
										 .request()
										 .header("Authorization", "Bearer " + token)
										 .get();
		
		if (loggedUserResponse.getStatus() == HttpStatus.OK.value())
		{
			User loggedUser = loggedUserResponse.readEntity(User.class);
			session.setUsername(loggedUser.getUsername());
			
			Response loggedContactResponse = ClientBuilder.newClient()
					 								.target(loggedUser.getLink("contact").getHref())
													.request()
													.header("Authorization", "Bearer " + token)
													.get();
			
			if (loggedContactResponse.getStatus() == HttpStatus.OK.value())
			{
				Contact loggedContact = loggedContactResponse.readEntity(Contact.class);
				session.setUserFirstName(loggedContact.getFirstName());
				session.setUserLastName(loggedContact.getLastName());
			}
			else
			{
				// TODO: Return an error to the user
				System.out.println("===> Error while getting the logged contact: " + loggedContactResponse.getStatus());				
			}
			
		}
		else
		{
			// TODO: Return an error to the user
			System.out.println("===> Error while getting the logged user: " + loggedUserResponse.getStatus());
		}

		// After obtaining the necessary information for the session, redirect to the initial user intended url
		return new ModelAndView("redirect:" + httpRequest.getSession().getAttribute("initial_request"));
		
	}
}

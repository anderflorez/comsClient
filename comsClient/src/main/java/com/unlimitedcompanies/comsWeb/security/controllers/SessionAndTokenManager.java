package com.unlimitedcompanies.comsWeb.security.controllers;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
import com.unlimitedcompanies.comsWeb.config.UserSessionManager;
import com.unlimitedcompanies.comsWeb.security.representations.LoggedUserInfo;

@Controller
public class SessionAndTokenManager
{
	@Autowired
	UserSessionManager session;
	
	@RequestMapping("/tokenmanager")
	public ModelAndView requestToken(@RequestParam("code") String code) throws IOException
	{
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

		
		// REST request to obtain the information about the current logged user and save it in the session		
		url = "http://localhost:8080/comsws/rest/loggedUserInfo";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		headers.setContentType(MediaType.APPLICATION_XML);
		HttpEntity<LoggedUserInfo> requestUserDetails = new HttpEntity<>(headers);
		ResponseEntity<LoggedUserInfo> userInfo = template.exchange(url, HttpMethod.GET, requestUserDetails, LoggedUserInfo.class);
		
		session.setUsername(userInfo.getBody().getUsername());
		session.setUserFirstName(userInfo.getBody().getuserFirstName());
		session.setUserLastName(userInfo.getBody().getuserLastName());
				
		return new ModelAndView("redirect:" + session.getInitialRequest());
	}
}

package com.unlimitedcompanies.comsClient.security.controllers;

import java.io.IOException;
import java.util.Base64;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
import com.unlimitedcompanies.comsClient.security.representations.ContactCollection;

@Controller
public class ReadSecurityObjectControllers
{
	@RequestMapping("/contacts")
	public ModelAndView displayContacts(@RequestParam("code") String code) throws IOException
	{
		//Required parameters in method - @RequestParam("code") String code
		
		
		
		
//		//Third attempt
//		
//		String url = "http://localhost:8080/comsws/oauth2/token";
//		String requestBody = "&code=" + code;
//			requestBody += "&client_id=comsClient";
//			requestBody += "&client_secret=somesecret";
//			requestBody += "&redirect_uri=" + URLEncoder.encode("http://localhost:8080/coms/contacts", "UTF-8");
//			requestBody += "&grant_type=authorization_code";
//			
//		Form form = new Form();
//		form.param("client_id", "comsClient");
//		form.param("client_secret", "somesecret");
//		form.param("code", code);
//		form.param("grant_type", "authorization_code");
//		form.param("redirect_uri", URLEncoder.encode("http://localhost:8080/coms/contacts", "UTF-8"));
////		form.param("scope", "Scope1");
//			
//		String credentials = "comsClient:somesecret";
//		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
//		
//		Client client = ClientBuilder.newClient();
//		WebTarget tokenRequestTarget = client.target(url);
//		
//		Response response = tokenRequestTarget
//				.request()
////				.header("Authorization", "Basic " + encodedCredentials)
//				.post(Entity.form(form));
//		
//		System.out.println("=============================================");
//		System.out.println(response);
//		System.out.println("=============================================");
		
		
		
		
		
		RestTemplate template = new RestTemplate();
		template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		
		String credentials = "comsClient:somesecret";
		String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
		
		String url = "http://localhost:8080/comsws/oauth/token";
	
		HttpHeaders headers1 = new HttpHeaders();
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("Authorization", "Basic " + encodedCredentials);
		params.add("code", code);
		params.add("grant_type", "authorization_code");
		params.add("redirect_uri", "http://localhost:8080/coms/contacts");
		params.add("client_id", "comsClient");
		headers1.addAll(params);
		
		HttpEntity<String> request = new HttpEntity<>(headers1);
		
		System.out.println("=============================================");
		System.out.println(request + "\n\n");
		
		ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, request, String.class);
				
		
		// leg 3
		
		url = "http://localhost:8080/comsws/rest/contacts";
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(response.getBody());
		String token = node.path("access_token").asText();
		
		System.out.println("=============================================");
		System.out.println(token + "\n\n");
		
		HttpHeaders headersLeg3 = new HttpHeaders();
		headersLeg3.add("Authorization", "Bearer " + token);
		HttpEntity<ContactCollection> requestLeg3 = new HttpEntity<>(headersLeg3);		
		ResponseEntity<ContactCollection> contacts = template.exchange(url, HttpMethod.GET, requestLeg3, ContactCollection.class);
		
		return new ModelAndView("/importedContacts.jsp", "contacts", contacts.getBody().getContacts());
	}
}

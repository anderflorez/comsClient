package com.unlimitedcompanies.comsClient.security.controllers;

import java.io.IOException;
import java.util.Base64;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		
		HttpHeaders headers1 = new HttpHeaders();
		headers1.add("Authorization", "Basic " + encodedCredentials);
		
		HttpEntity<String> request = new HttpEntity<>(headers1);
		
		String url = "http://localhost:8080/comsws/oauth/token";
		url += "?code=" + code;
		url += "&grant_type=authorization_code";
		url += "&redirect_uri=http://localhost:8080/coms/contacts";
		url += "&client_id=comsClient";
		
		System.out.println("\n\n========> Headers before request: \n" + request + "\n\n");
	
		ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, request, String.class);
		
		System.out.println("\n\n\n" + response.getBody() + "\n\n\n");
		
		
		
		
		url = "http://localhost:8080/comsws/rest/contacts";
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(response.getBody());
		String token = node.path("access_token").asText();
		
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + token);
		HttpEntity<String> contactRequest = new HttpEntity<String>(headers2);
		
		System.out.println("\n\n========> url request: \n" + url);
		System.out.println("========> headers before request: \n" + contactRequest + "\n\n");
		

		try
		{
			ResponseEntity<String> contactsResponseEntity = template.exchange(url, HttpMethod.GET, contactRequest, String.class);
			System.out.println("\n\n\n" + contactsResponseEntity.getBody() + "\n\n\n");

		} catch (OAuth2Exception e)
		{
			System.out.println("\n\n========> Exception occurred:\n");
			System.out.println(e.getMessage());
//			e.printStackTrace();
		}
				
		return null;//new ModelAndView("/importedContacts.jsp", "contacts", contactsResponseEntity.getBody().getContacts());
	}
}

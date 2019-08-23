package com.unlimitedcompanies.comsWeb.cli;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.unlimitedcompanies.comsWeb.representations.security.Contact;
import com.unlimitedcompanies.comsWeb.representations.security.ContactCollection;

public class RestClient
{

	public static void main(String[] args) 
	{
		try
		{
			RestTemplate template = new RestTemplate();
			
			String originalUserPass = "administrator:uec123";
			String encodedData = Base64.getEncoder().encodeToString(originalUserPass.getBytes());
			
			System.out.println(encodedData);
			
			List<MediaType> acceptableMediaTypes = new ArrayList<>();
			acceptableMediaTypes.add(MediaType.APPLICATION_XML);
			acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(acceptableMediaTypes);
			headers.add("Authorization", "Basic " + encodedData);
			
			HttpEntity<Contact> requestEntity = new HttpEntity<>(headers);
		
			ResponseEntity<Contact> response = template.exchange("http://localhost:8080/comsws/contact/63a73cdf-ba69-4b86-a32d-160284c1cc7e", 
															HttpMethod.GET, 
															requestEntity, 
															Contact.class);
			System.out.println(response.getBody());
			
			//Second Request

			ResponseEntity<ContactCollection> contacts = template.exchange("http://localhost:8080/comsws/contacts", HttpMethod.GET, requestEntity, ContactCollection.class);
			System.out.println(contacts.getBody());
		} 
		
		catch (HttpClientErrorException.NotFound e)
		{		
			
			System.out.println(e.getStatusCode() + " - " + e.getResponseBodyAsString());
			if (e.getResponseHeaders().containsKey("message"))
			{
				for (String msg : e.getResponseHeaders().get("message"))
				{
					System.out.println(msg);
				}
			}
		}
		
		catch (HttpClientErrorException e)
		{		
			System.out.println("An internal exception has occured. \nHttp status code: " + e.getStatusCode());
		}
		
		
	}

}

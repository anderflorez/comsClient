package com.unlimitedcompanies.comsClient.cli;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.unlimitedcompanies.comsClient.security.representations.Contact;

public class RestClient
{

	public static void main(String[] args) 
	{
		RestTemplate template = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		List<MediaType> acceptableMediaTypes = new ArrayList<>();
		acceptableMediaTypes.add(MediaType.IMAGE_JPEG);
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		headers.setAccept(acceptableMediaTypes);
		
		HttpEntity<HttpHeaders> requestEntity = new HttpEntity<HttpHeaders>(headers);
		HttpEntity<String> response = template.exchange("http://localhost:8080/comsws/contact/63a73cdf-ba69-4b86-a32d-160284c1cc7e", 
														HttpMethod.GET, 
														requestEntity, 
														String.class);
		
		System.out.println(response.getBody());
		
//		Contact contact = template.getForObject("http://localhost:8080/comsws/contact/63a73cdf-ba69-4b86-a32d-160284c1cc7e", Contact.class);
//		System.out.println("Contact: " + contact);
	}

}

package com.unlimitedcompanies.comsWeb.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

// OAuth2 Client configuration disabled
//@Configuration
//@EnableOAuth2Client
public class OAuth2ResourceConfig
{
	@Autowired
	private OAuth2ClientContext oauth2Context;	
	
//	@Bean
	public OAuth2ProtectedResourceDetails resource() 
	{
		List<String> scopes = new ArrayList<>();
		scopes.add("trusted");
		scopes.add("read");
		
		AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
		details.setClientId("comsClient");
		details.setUserAuthorizationUri("http://localhost:8080/comsws/oauth/authorize");
		details.setAccessTokenUri("http://localhost:8080/comsws/oauth/token");
		details.setClientSecret("somesecret");
		details.setGrantType("authorization_code");
		details.setPreEstablishedRedirectUri("http://localhost:8080/coms/contacts");
		details.setScope(scopes);
		
		return details;
	}
	
//	@Bean
	public OAuth2RestTemplate comsClientRestTemplate()
	{
		return new OAuth2RestTemplate(resource(), oauth2Context);
	}
}

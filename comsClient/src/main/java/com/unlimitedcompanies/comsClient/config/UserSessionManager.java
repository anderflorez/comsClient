package com.unlimitedcompanies.comsClient.config;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSessionManager
{
	private String token;
	private String initialRequest;
	private String username;

	public UserSessionManager()
	{
		this.token = null;
	}
	
	public String getToken() throws NullTokenException
	{
		if (this.token == null)
		{
			throw new NullTokenException();
		}
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}


	public String getInitialRequest()
	{
		return initialRequest;
	}


	public void setInitialRequest(String initialRequest)
	{
		this.initialRequest = initialRequest;
	}
	
	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getAuthCodeRedirectUrl(String request)
	{
		this.setInitialRequest(request);
		return "redirect:"
				+ "http://localhost:8080/comsws/oauth/authorize"
				+ "?response_type=code"
				+ "&client_id=comsClient"
				+ "&redirect_uri=http://localhost:8080/coms/tokenmanager"
				+ "&scope=trusted";
	}
}

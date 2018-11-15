package com.unlimitedcompanies.comsWeb.config;

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
	private String userFirstName;
	private String userLastName;

	public UserSessionManager()
	{
		this.token = null;
		this.username = null;
	}
	
	public String getToken() throws NullOrIncompleteSessionException
	{
		this.checkSession();
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
		if (initialRequest == null || initialRequest.isEmpty())
		{
			this.initialRequest = "/";
		}
		else 
		{
			this.initialRequest = initialRequest;			
		}
	}
	
	public String getUsername() throws NullOrIncompleteSessionException
	{
		this.checkSession();
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getUserFirstName() throws NullOrIncompleteSessionException
	{
		this.checkSession();
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName)
	{
		this.userFirstName = userFirstName;
		if (this.userFirstName == null)
		{
			this.userFirstName = "";
		}
	}

	public String getUserLastName()
	{
		return userLastName;
	}

	public void setUserLastName(String userLastName)
	{
		this.userLastName = userLastName;
		if (this.userLastName == null)
		{
			this.userLastName = "";
		}
	}
	
	public String getLogedUserFullName() throws NullOrIncompleteSessionException
	{
		return this.getUserFirstName() + " " + this.getUserLastName();
	}
	
	public void checkSession() throws NullOrIncompleteSessionException
	{
		if (this.token == null || this.username == null)
		{
			throw new NullOrIncompleteSessionException();
		}
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
	
	public void clearSession()
	{
		this.token = null;
		this.initialRequest = null;
		this.username = null;
		this.userFirstName = null;
		this.userLastName = null;
	}
}

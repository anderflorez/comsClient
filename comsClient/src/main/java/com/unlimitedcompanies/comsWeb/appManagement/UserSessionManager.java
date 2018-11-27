package com.unlimitedcompanies.comsWeb.appManagement;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSessionManager
{
	private String token;
	private String username;
	private String userFirstName;
	private String userLastName;
	private String initialRequest;

	public UserSessionManager()
	{
		this.token = null;
		this.username = null;
		this.userFirstName = null;
	}
	
	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}
	
	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getUserFirstName()
	{
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
		
	public String getInitialRequest()
	{
		return initialRequest;
	}

	public void setInitialRequest(String initialRequest)
	{
		this.initialRequest = initialRequest;
	}

	public String getLogedUserFullName()
	{
		return this.getUserFirstName() + " " + this.getUserLastName();
	}
	
	public boolean checkSession()
	{
		if (this.token == null || this.username == null || this.userFirstName == null)
		{
			return false;
		}
		return true;
	}
	
	public void clearSession()
	{
		this.token = null;
		this.username = null;
		this.userFirstName = null;
		this.userLastName = null;
	}
}

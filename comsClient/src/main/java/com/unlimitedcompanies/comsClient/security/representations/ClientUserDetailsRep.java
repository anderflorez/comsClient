package com.unlimitedcompanies.comsClient.security.representations;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
public class ClientUserDetailsRep
{
	String username;
	
	public ClientUserDetailsRep()	{}

	public ClientUserDetailsRep(String username)
	{
		this.username = username;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}
}

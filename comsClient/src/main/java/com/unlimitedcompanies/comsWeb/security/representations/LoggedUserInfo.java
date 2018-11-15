package com.unlimitedcompanies.comsWeb.security.representations;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="userDetailsRep")
public class LoggedUserInfo
{
	String username;
	String userFirstName;
	String userLastName;

	public LoggedUserInfo() {}
	
	public LoggedUserInfo(String username, String contactFirstName, String contactLastName)
	{
		this.username = username;
		this.userFirstName = contactFirstName;
		this.userLastName = contactLastName;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getuserFirstName()
	{
		return userFirstName;
	}

	public void setuserFirstName(String userFirstName)
	{
		this.userFirstName = userFirstName;
	}

	public String getuserLastName()
	{
		return userLastName;
	}

	public void setuserLastName(String userLastName)
	{
		this.userLastName = userLastName;
	}

}

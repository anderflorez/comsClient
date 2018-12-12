package com.unlimitedcompanies.comsWeb.security.representations;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.ResourceSupport;

@XmlRootElement(name = "user")
public class User extends ResourceSupport
{	
	private Integer userId;
	private String username;
	private char[] password;
	private char[] passwordcheck;
	private boolean enabled;
	private String dateAdded;
	private String lastAccess;
	private Integer contactId;
	
	public User() {}

	public Integer getUserId()
	{
		return userId;
	}

	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public char[] getPassword()
	{
		return password;
	}

	public void setPassword(char[] password)
	{
		this.password = password;
	}
	
	public char[] getPasswordcheck()
	{
		return passwordcheck;
	}

	public void setPasswordcheck(char[] passwordcheck)
	{
		this.passwordcheck = passwordcheck;
	}

	public boolean getEnabled()
	{
		return enabled;
	}
	
	public String getEnabledStatus()
	{
		return this.enabled ? "Active" : "Inactive";
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public String getDateAdded()
	{
		return dateAdded;
	}

	public void setDateAdded(String dateAdded)
	{
		this.dateAdded = dateAdded;
	}

	public String getLastAccess()
	{
		return lastAccess;
	}

	public void setLastAccess(String lastAccess)
	{
		this.lastAccess = lastAccess;
	}

	public Integer getContactId()
	{
		return contactId;
	}

	public void setContactId(Integer contactId)
	{
		this.contactId = contactId;
	}
	
}

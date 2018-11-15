package com.unlimitedcompanies.comsClient.security.representations;

import java.time.ZonedDateTime;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User
{
	private Integer userId;
	private String username;
	private String password;
	private String enabled;
	private ZonedDateTime dateAdded;
	private ZonedDateTime lastAccess;
	private Contact contact;
	
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

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getEnabled()
	{
		return enabled;
	}

	public void setEnabled(String enabled)
	{
		this.enabled = enabled;
	}

	public ZonedDateTime getDateAdded()
	{
		return dateAdded;
	}

	public void setDateAdded(ZonedDateTime dateAdded)
	{
		this.dateAdded = dateAdded;
	}

	public ZonedDateTime getLastAccess()
	{
		return lastAccess;
	}

	public void setLastAccess(ZonedDateTime lastAccess)
	{
		this.lastAccess = lastAccess;
	}

	public Contact getContact()
	{
		return contact;
	}

	public void setContact(Contact contact)
	{
		this.contact = contact;
	}
		
}

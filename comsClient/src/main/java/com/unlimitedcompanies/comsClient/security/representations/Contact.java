package com.unlimitedcompanies.comsClient.security.representations;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Contact
{
	private String contactId;
	private String email;
	private String firstName;

	public String getContactId()
	{
		return contactId;
	}

	public void setContactId(String contactId)
	{
		this.contactId = contactId;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	public String toString()
	{
		return this.contactId;
	}

}

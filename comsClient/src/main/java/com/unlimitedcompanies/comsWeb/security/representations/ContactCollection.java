package com.unlimitedcompanies.comsWeb.security.representations;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.unlimitedcompanies.comsWeb.appManagement.ResponseFacade;

@XmlRootElement(name = "response")
public class ContactCollection extends ResponseFacade
{	
	private List<Contact> contact;

	public ContactCollection() {}

	@XmlElement(name = "contact")
	public List<Contact> getContacts()
	{
		return contact;
	}

	public void setContacts(List<Contact> contacts)
	{
		this.contact = contacts;
	}
}

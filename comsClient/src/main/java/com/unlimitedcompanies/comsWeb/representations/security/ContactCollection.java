package com.unlimitedcompanies.comsWeb.representations.security;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.unlimitedcompanies.comsWeb.appManagement.ResponseFacade;

@XmlRootElement(name = "contacts")
public class ContactCollection extends ResponseFacade
{	
	@XmlElement(name = "contact")
	private List<Contact> contact;
	private Integer prevPage;
	private Integer nextPage;

	public ContactCollection() 
	{
		this.contact = new ArrayList<>();
		this.prevPage = null;
		this.nextPage = null;
	}

	public List<Contact> getContacts()
	{
		return contact;
	}

	public void setContacts(List<Contact> contacts)
	{
		this.contact = contacts;
	}

	public Integer getPrevPage()
	{
		return prevPage;
	}

	public void setPrevPage(Integer prevPage)
	{
		this.prevPage = prevPage;
	}

	public Integer getNextPage()
	{
		return nextPage;
	}

	public void setNextPage(Integer nextPage)
	{
		this.nextPage = nextPage;
	}
	
}

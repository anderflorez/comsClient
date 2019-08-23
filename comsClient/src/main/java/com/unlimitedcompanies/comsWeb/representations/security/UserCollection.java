package com.unlimitedcompanies.comsWeb.representations.security;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.unlimitedcompanies.comsWeb.appManagement.ResponseFacade;

@XmlRootElement(name = "users")
public class UserCollection extends ResponseFacade
{
	List<User> users;
	private Integer prevPage;
	private Integer nextPage;
	
	public UserCollection()
	{
		this.users = new ArrayList<>();
		this.prevPage = null;
		this.nextPage = null;
	}

	@XmlElement(name = "user")
	public List<User> getUsers()
	{
		return users;
	}

	public void setUsers(List<User> users)
	{
		this.users = users;
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

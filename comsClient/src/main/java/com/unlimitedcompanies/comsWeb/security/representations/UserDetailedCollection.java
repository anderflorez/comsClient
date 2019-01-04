package com.unlimitedcompanies.comsWeb.security.representations;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.ResourceSupport;

@XmlRootElement(name = "usersDetailed")
public class UserDetailedCollection extends ResourceSupport
{
	private List<UserDetailedDTO> users;
	
	public UserDetailedCollection()
	{
		this.users = new ArrayList<>();
	}

	@XmlElement(name = "userDetailed")
	public List<UserDetailedDTO> getUsers()
	{
		return this.users;
	}

	public void setUsers(List<UserDetailedDTO> users)
	{
		this.users = users;
	}
}

package com.unlimitedcompanies.comsWeb.security.representations;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.ResourceSupport;

@XmlRootElement(name = "roles")
public class RoleCollectionResponse extends ResourceSupport
{
	private List<Role> roles;
	private Integer next;
	private Integer prev;
	
	public RoleCollectionResponse() 
	{
		this.roles = new ArrayList<>();
		this.next = null;
		this.prev = null;
	}

	@XmlElement(name = "role")
	public List<Role> getRoles()
	{
		return roles;
	}

	public void setRoles(List<Role> roles)
	{
		this.roles = roles;
	}

	public Integer getNext()
	{
		return next;
	}

	public void setNext(Integer next)
	{
		this.next = next;
	}

	public Integer getPrev()
	{
		return prev;
	}

	public void setPrev(Integer prev)
	{
		this.prev = prev;
	}
	
}

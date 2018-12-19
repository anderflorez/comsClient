package com.unlimitedcompanies.comsWeb.security.representations;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.ResourceSupport;

@XmlRootElement(name = "user")
public class UserPassword extends ResourceSupport
{
	private Integer userId;
	private char[] oldPassword;
	private char[] newPassword;
	
	public UserPassword() {}

	public Integer getUserId()
	{
		return userId;
	}

	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}

	public char[] getOldPassword()
	{
		return oldPassword;
	}

	public void setOldPassword(char[] oldPassword)
	{
		this.oldPassword = oldPassword;
	}

	public char[] getNewPassword()
	{
		return newPassword;
	}

	public void setNewPassword(char[] newPassword)
	{
		this.newPassword = newPassword;
	}

}

package com.unlimitedcompanies.comsWeb.representations.security;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.ResourceSupport;

@XmlRootElement(name = "user")
public class UserPassword extends ResourceSupport
{
	private Integer userId;
	private char[] oldPassword;
	private char[] newPassword;
	private char[] confirmPassword;

	public UserPassword() {}

	public UserPassword(Integer userId, char[] oldPassword, char[] newPassword, char[] confirmPassword)
	{
		this.userId = userId;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
		this.confirmPassword = confirmPassword;
	}

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

	public char[] getConfirmPassword()
	{
		return confirmPassword;
	}

	public void setConfirmPassword(char[] confirmPassword)
	{
		this.confirmPassword = confirmPassword;
	}

}

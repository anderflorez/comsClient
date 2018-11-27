package com.unlimitedcompanies.comsWeb.appManagement;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.ResourceSupport;

@XmlRootElement
public class ResponseFacade extends ResourceSupport
{
	private int error_code;
	private String error_message;
	
	public ResponseFacade() {}

	public int getErrorCode()
	{
		return error_code;
	}

	public void setErrorCode(int errorCode)
	{
		this.error_code = errorCode;
	}

	public String getErrorMessage()
	{
		return error_message;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.error_message = errorMessage;
	}
}

package com.unlimitedcompanies.comsWeb.appManagement;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.ResourceSupport;

@XmlRootElement
public class ResponseFacade extends ResourceSupport
{
	private int statusCode;
	private String success;
	private List<String> errors;
	private List<String> messages;
	
	public ResponseFacade() {}

	public int getStatusCode()
	{
		return statusCode;
	}

	public void setStatusCode(int statusCode)
	{
		this.statusCode = statusCode;
	}

	public String getSuccess()
	{
		return success;
	}

	public void setSuccess(String success)
	{
		this.success = success;
	}

	public List<String> getErrors()
	{
		return errors;
	}

	public void setErrors(List<String> errors)
	{
		this.errors = errors;
	}
	
	public void addErrorMessage(String errorMessage)
	{
		this.errors.add(errorMessage);
	}

	public List<String> getMessages()
	{
		return messages;
	}

	public void setMessages(List<String> messages)
	{
		this.messages = messages;
	}
}

package com.unlimitedcompanies.comsWeb.security.representations;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.springframework.hateoas.ResourceSupport;

public class ErrorMessages extends ResourceSupport
{	
	private int statusCode;
	
	@XmlElement
	private List<String> errors;
	
	@XmlElement
	private List<String> messages;
	
	public ErrorMessages()
	{
		this.statusCode = 0;
		this.errors = new ArrayList<>();
		this.messages = new ArrayList<>();
	}

	public int getStatusCode()
	{
		return statusCode;
	}

	public void setStatusCode(int statusCode)
	{
		this.statusCode = statusCode;
	}

	public List<String> getErrors()
	{
		return this.errors;
	}
	
	public void addError(String error)
	{
		this.errors.add(error);
	}

	public List<String> getMessages()
	{
		return this.messages;
	}

	public void addMessage(String message)
	{
		this.messages.add(message);
	}
	
}

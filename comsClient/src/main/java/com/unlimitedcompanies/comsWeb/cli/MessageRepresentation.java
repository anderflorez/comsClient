package com.unlimitedcompanies.comsWeb.cli;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageRepresentation
{
	private String message;

	public MessageRepresentation() {}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

}

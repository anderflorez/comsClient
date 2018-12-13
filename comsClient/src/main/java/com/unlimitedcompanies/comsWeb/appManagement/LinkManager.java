package com.unlimitedcompanies.comsWeb.appManagement;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class LinkManager
{
	private Map<String, String> baseLinks;
	
	public LinkManager()
	{
		this.baseLinks = new HashMap<>();
	}
	
	public Map<String, String> getBaseLinks()
	{
		return Collections.unmodifiableMap(baseLinks);
	}

	public void addBaseLink(String key, String link)
	{
		this.baseLinks.put(key, link);
	}
	
	public String getBaseLink(String linkKey) throws LinkNotFoundException
	{
		String link = this.baseLinks.get(linkKey);
		if (link == null)
		{
			throw new LinkNotFoundException();
		}
		return link;
	}
}

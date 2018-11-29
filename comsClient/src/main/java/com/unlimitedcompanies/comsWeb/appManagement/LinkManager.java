package com.unlimitedcompanies.comsWeb.appManagement;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class LinkManager
{
	private Map<String, String> baseLinks;
	private Map<String, Map<Integer, String>> links;
	
	public LinkManager()
	{
		this.baseLinks = new HashMap<>();
		this.links = new HashMap<>();
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
	
	public void addLink(String objectType, Integer objectId, String uri)
	{
		if (this.links.containsKey(objectType))
		{
			this.links.get(objectType).put(objectId, uri);
		}
		else
		{
			this.links.put(objectType, new HashMap<>());
			this.links.get(objectType).put(objectId, uri);
		}
	}
	
	public String getLink(String objectType, Integer objectId) throws LinkNotFoundException
	{
		Map<Integer, String> link = this.links.get(objectType);
		if (link != null && link.get(objectId) != null)
		{
			return link.get(objectId);
		}
		else
		{
			throw new LinkNotFoundException();
		}
	}
	
	public void clearObjectType(String objectType)
	{
		// TODO: test this method
		
		Map<Integer, String> tempMap;
		tempMap = this.links.get(objectType);
		if (!tempMap.isEmpty())
		{
			tempMap.clear();
		}
	}
}

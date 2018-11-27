package com.unlimitedcompanies.comsWeb.appManagement;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class LinkManager
{
	private Map<String, Map<Integer, String>> links;
	
	public LinkManager()
	{
		this.links = new HashMap<>();
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
	
	public String getLink(String objectType, Integer objectId)
	{
		Map<Integer, String> link = this.links.get(objectType);
		if (link != null)
		{
			return link.get(objectId);
		}
		return null;
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

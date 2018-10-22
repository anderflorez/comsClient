package comsClient;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "contacts")
public class ContactCollectionRepresentation
{
	private List<Contact> contacts;

	public ContactCollectionRepresentation() {}

	public ContactCollectionRepresentation(List<Contact> contacts)
	{
		super();
		this.contacts = contacts;
	}

	@XmlElement(name = "contact")
	public List<Contact> getContacts()
	{
		return contacts;
	}

	public void setContacts(List<Contact> contacts)
	{
		this.contacts = contacts;
	}

	public String toString()
	{
		return "The customers are " + contacts.toString();
	}

}

package comsClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class RestClient
{

	public static void main(String[] args) throws IOException
	{
		RestTemplate template = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		List<MediaType> acceptableMediaTypes = new ArrayList<>();
		acceptableMediaTypes.add(MediaType.IMAGE_JPEG);
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		headers.setAccept(acceptableMediaTypes );
		
		HttpEntity<HttpHeaders> requestEntity = new HttpEntity<HttpHeaders>(headers);
		try
		{
//			HttpEntity<String> response = template.exchange("http://localhost:8080/comsWS/contact/80b3f572-b9d1-4dc3-bf62-6216ec973630--knlkin",
//													HttpMethod.GET, requestEntity, String.class);
			HttpEntity<String> response = template.exchange("http://localhost:8080/comsWS/contacts",
					HttpMethod.GET, requestEntity, String.class);
			System.out.println(response.getBody());
		} 
		catch (HttpClientErrorException e)
		{
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
			{
				System.out.println("Customer was not found");				
			}
			else
			{
				System.out.println("An unknown error has occurred");
			}
		}
		
	}

}

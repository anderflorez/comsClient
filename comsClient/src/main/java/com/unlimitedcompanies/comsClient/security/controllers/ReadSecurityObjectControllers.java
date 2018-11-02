package com.unlimitedcompanies.comsClient.security.controllers;

import java.io.IOException;
import java.net.URLEncoder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReadSecurityObjectControllers
{
	@RequestMapping("/contacts")
	public ModelAndView displayContacts(@RequestParam("code") String code) throws ClientProtocolException, IOException
	{
		//Third attempt
		
		String url = "http://localhost:8080/comsws/oauth2/token";
		String requestBody = "&code=" + code;
			requestBody += "&client_id=comsClient";
			requestBody += "&client_secret=somesecret";
			requestBody += "&redirect_uri=" + URLEncoder.encode("http://localhost:8080/coms/contacts", "UTF-8");
			requestBody += "&grant_type=authorization_code";
			
		Form form = new Form();
		form.param("client_id", "comsClient");
		form.param("client_secret", "somesecret");
		form.param("code", code);
		form.param("grant_type", "authorization_code");
		form.param("redirect_uri", URLEncoder.encode("http://localhost:8080/coms/contacts", "UTF-8"));
//		form.param("scope", "Scope1");
			
		String credentials = "comsClient:somesecret";
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
		
		Client client = ClientBuilder.newClient();
		WebTarget tokenRequestTarget = client.target(url);
		
		Response response = tokenRequestTarget
				.request()
//				.header("Authorization", "Basic " + encodedCredentials)
				.post(Entity.form(form));
		
		System.out.println("=============================================");
		System.out.println(response);
		System.out.println("=============================================");
		
		
		
		
		
//		//Second Attempt
//		String url = "http://localhost:8080/comsws/oauth/token";
////		url += "?grant_type=authorization_code";
////		url += "&code=" + code;
////		url += "&redirect_uri=http://localhost:8080/coms/contacts";
////		url += "&client_id=comsClient";
//		
//		String credentials = "comsClient:somesecret";
//		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
//		
//		List<NameValuePair> params = new ArrayList<>();
//		params.add(new BasicNameValuePair("grant_type", "authorization_code"));
//		params.add(new BasicNameValuePair("code", code));
//		params.add(new BasicNameValuePair("redirect_uri", "http://localhost:8080/coms/contacts"));
//		params.add(new BasicNameValuePair("client_id", "comsClient"));
//		params.add(new BasicNameValuePair("Authorization", "Basic " + encodedCredentials));
//		
//		HttpClient httpClient = HttpClients.createDefault();
//		HttpPost httpPost = new HttpPost(url);
//		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
//		
//		HttpResponse response = httpClient.execute(httpPost);
//		HttpEntity entity = response.getEntity();
//		
//		if (entity != null)
//		{
//			try (InputStream instream = entity.getContent())
//			{
//				System.out.println("=============================================");
//				System.out.println(instream);
//				Reader reader = new InputStreamReader(instream);
//				BufferedReader br = new BufferedReader(reader);
//				System.out.println(br.readLine());
//				
//			} catch (UnsupportedOperationException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		
		
		
		
		
		
		
		
		
		
		
//		ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//		
//		
//		
//		
//		RestTemplate template = new RestTemplate(requestFactory);
//		
//		String credentials = "comsClient:somesecret";
//		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes())); //Base64.getEncoder().encodeToString(credentials.getBytes());
//		
//		List<MediaType> acceptableMediaTypes = new ArrayList<>();
//		acceptableMediaTypes.add(MediaType.APPLICATION_XML);
//		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
//		
//		HttpHeaders headers = new HttpHeaders();
//		headers.setAccept(acceptableMediaTypes);
//		headers.add("Authorization", "Basic " + encodedCredentials);
//		HttpEntity<String> request = new HttpEntity<>(headers);
//		
//		System.out.println("=================> Headers: " + request.getHeaders());
//		
//		String url = "http://localhost:8080/comsws/oauth/token";
//		url += "?grant_type=authorization_code";
//		url += "&code=" + code;
//		url += "&redirect_uri=http://localhost:8080/coms/contacts";
//		url += "&client_id=comsClient";
//		
//		System.out.println("=================> url: " + url);
//		
//		ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, request, String.class);
//		
//		System.out.println("=============================================");
//		System.out.println("\n\n\n" + response.getBody() + "\n\n\n");
		System.out.println("=============================================");
		System.out.println("Finished running the client");
		
		return null;
	}
}

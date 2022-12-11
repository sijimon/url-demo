package com.sijimon;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;


public class TestURL {

	public static void main(String[] args) throws Exception {

		
		try {
			URL url = new URL("https://siji@www.yahoo.com:89080/test/test2?a=b&c=d");
			
			System.out.println("url.getAuthority:"+url.getAuthority());
			System.out.println("url.getDefaultPort:"+url.getDefaultPort());			
			System.out.println("url.getFile:"+url.getFile());
			System.out.println("url.getPath:"+url.getPath());
			System.out.println("url.getPort:"+url.getPort());			
			System.out.println("url.getProtocol:"+url.getProtocol());
			System.out.println("url.getQuery:"+url.getQuery());			
			System.out.println("url.getRef:"+url.getRef());
			System.out.println("url.getUserInfo:"+url.getUserInfo());
			//System.out.println("url.getContent:"+url.getContent());			

			//URL nurl = new URL(url.openConnection().,"test1")
			
			/*
			URIBuilder builder = new URIBuilder()
				    .setScheme("http")
				    .setHost("apache.org")
				    .setPath("/shindig")
				    .addParameter("helloWorld", "foo&bar")
				    .setFragment("foo");
				builder.toString();
				*/
			
			
			String baseURL = "https://www.yahoo.com:89080/test2";//?a=b&c=d";
			
			String payload = "user=sijimon&password=testpass";
			
			System.out.println("url.getQuery:"+url.getQuery());			

			/*
				String final_URL = UriComponentsBuilder
			    .fromUriString(baseURL)
			    .queryParam("SLP", payload)
			    .build().toUriString();
				
				System.out.println("final_URL:"+final_URL);
			
				URL nurl = new URL(final_URL);
				;
				
				System.out.println(nurl.getQuery());
				
				System.out.println("Final:"+StringUtils.substringAfter(nurl.getQuery(), "SLP="));
				
				Map<String, String> mm = UriComponentsBuilder.newInstance().query(nurl.getQuery()).build().getQueryParams().toSingleValueMap();
				
				System.out.println(mm.toString());
				
				System.out.println(mm.remove("SLP"));				
				
				System.out.println(mm.size());	
				
				*/
				
			//	UriComponentsBuilder.newInstance().fromUriString(nurl.get)
				AESCryptoUtility aesu = new AESCryptoUtility();
				
				String encriptPayLoad = aesu.encrypt("Test", payload);
				
				System.out.println("encriptPayLoad:"+encriptPayLoad);
				
				
				String final_URL = UriComponentsBuilder
					    .fromUriString(baseURL)
					    .queryParam("SLP", encriptPayLoad)					    
					    .build()
					    .encode()
					    .toUriString();
				
				System.out.println("Final_URL:"+final_URL);
				
				/*
				 * UriComponentsBuilder .fromUriString(url) .build() .encode() .toUri()
				 */
				//Final_URL:https://www.yahoo.com:89080/test2?SLP=xSgG9IxyiE3VXb+7gMy6/cZGfvb+xoZAxFla99+i2EqkCZdrUWtBROdJpM3YDTWf6woC/EsWhG7FDcUaG1vqE+ZAH9CU8EXuI9g=
				//Final_URL:https://www.yahoo.com:89080/test2?SLP=ooBRf1KdkCmKLhUSugjchvmomVWIefY6oZpIGrQDSGT9d4SNa0ZaF5OGNBzTf1s5uNhINz3WAH2dNXQWdllTzFixK3lKJVwzQPQ%3D	
				//EncodeURL
				
				
				
				//Decode URL
				
				
				String urltodecrypt = java.net.URLDecoder.decode(final_URL, StandardCharsets.UTF_8.name());
				
				System.out.println("urltodecrypt:"+urltodecrypt);
				

				String tmpBaseURL = removeQueryParameter(urltodecrypt,"SLP");
				System.out.println(tmpBaseURL);
				
				String slpValue = getSLPValue(urltodecrypt,"SLP");

				System.out.println("slpValue:"+slpValue);
				
				String decriptedslp = aesu.decrypt("Test", payload);		
				System.out.println(decriptedslp);
				//xSgG9IxyiE3VXb+7gMy6/cZGfvb+xoZAxFla99+i2EqkCZdrUWtBROdJpM3YDTWf6woC/EsWhG7FDcUaG1vqE+ZAH9CU8EXuI9g=
				//xSgG9IxyiE3VXb+7gMy6/cZGfvb+xoZAxFla99+i2EqkCZdrUWtBROdJpM3YDTWf6woC/EsWhG7FDcUaG1vqE+ZAH9CU8EXuI9g=
				//System.out.println(aesu.encrypt("Test", "a=b&c=d"));
				//System.out.println(aesu.decrypt("Test", "rcpUdI4GO35gVG0CBBtbSPhX+Cf6278b4kft+Dabl1ouJj1KtCfVZSRtWUFzOp0BmRRa"));
				
						
			
/*					URI uri = new URI()
					
					
					UriBuilder builder = new URIBuilder(java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty(), null, java.util.Optional.empty(), null, false, false);
					builder.setScheme("http");
					builder.setHost("IP");
					builder.setPath("/foldername/1234");
					builder.addParameter("abc", "xyz");
					URL url = builder.build().toURL();
					
					
					
			final static String URL_FORMAT = "http://url.com?%s=%s";
			final String request = String.format(URL_FORMAT, "paramater", "hi");*/
					

		} catch (MalformedURLException e) {
			System.out.println("---:"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		
	}
	
	public static String removeQueryParameter(String url, String parameterName) throws URISyntaxException {
	    URIBuilder uriBuilder = new URIBuilder(url);
	    List<NameValuePair> queryParameters = uriBuilder.getQueryParams()
	              .stream()
	              .filter(p -> !p.getName().equals(parameterName))
	              .collect(Collectors.toList());
	    if (queryParameters.isEmpty()) {
	        uriBuilder.removeQuery();
	    } else {
	        uriBuilder.setParameters(queryParameters);
	    }
	    return uriBuilder.build().toString();
	}
	
	
	public static String getSLPValue(String url, String parameterName) throws URISyntaxException {
	    URIBuilder uriBuilder = new URIBuilder(url);
	    List<NameValuePair> queryParameters = uriBuilder.getQueryParams()
	              .stream()
	              .filter(p -> p.getName().equals(parameterName))
	              .collect(Collectors.toList());
	    if (queryParameters.isEmpty()) {
	        uriBuilder.removeQuery();
	    } else {
	        uriBuilder.setParameters(queryParameters);
	    }
	    return queryParameters.get(0).getValue();//uriBuilder.build().toString();
		
	}

}

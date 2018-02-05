package ru.jug.filter;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import ru.jug.Store;
import ru.jug.User;

@EnableConfigurationProperties( { ZuulProperties.class } )
public class PostFilter extends ZuulFilter {
	
	@Autowired
	private ServerProperties server;
	
	@Autowired
	private ZuulProperties zuulProperties;
	
	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() 
	{	
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
	    
	    // We're only interested in POSTs of new information
	    if (request.getMethod().indexOf("POST") == -1)
	    {
	    	return null;
	    }
	    
	    String path = request.getServletPath(); 

	    SimpleRouteLocator routeLocator = 
	    		new SimpleRouteLocator(this.server.getServletPrefix(), zuulProperties);
	    
	    Route route = routeLocator.getMatchingRoute("/reporting");
	    String serviceID = route.getLocation(); 
		
	    System.out.println("si: " + serviceID);
	    
	    MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	    headers.add("HeaderName", "value");
	    headers.add("Content-Type", "application/json");

	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	    
	    if (path.indexOf("store") > -1)
    	{
			try {
				Gson gson = new Gson();
				BufferedReader reader = request.getReader();
				Store store = gson.fromJson(reader, Store.class);
		    	reportStore(headers, restTemplate, serviceID + path, store);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	    else
	    {
	    	try {
				Gson gson = new Gson();
				BufferedReader reader = request.getReader();
				User user = gson.fromJson(reader, User.class);
				reportUser(headers, restTemplate, serviceID + path, user);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
	    
		return null;
	}
	
	private String reportUser(MultiValueMap<String, String> headers, 
			RestTemplate restTemplate, String fullPath, User objectToPass) 
	{
	    HttpEntity<User> outReq = new HttpEntity<User>(objectToPass, headers);
	    String response = restTemplate.postForObject(fullPath, outReq, String.class);
	    
		return response;
	}

	private String reportStore(MultiValueMap<String, String> headers, 
			RestTemplate restTemplate, String fullPath, Store objectToPass) 
	{
	    HttpEntity<Store> outReq = new HttpEntity<Store>(objectToPass, headers);
	    String response = restTemplate.postForObject(fullPath, outReq, String.class);
	    
		return response;
	}

}

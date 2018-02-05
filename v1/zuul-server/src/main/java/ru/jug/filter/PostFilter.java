package ru.jug.filter;

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
	public Object run() {
		
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
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
	    	reportStore(headers, restTemplate, serviceID + path);
    	}
	    else
	    {
	    	reportUser(headers, restTemplate, serviceID + path);
	    }
	    
		return null;
	}
	
	private String reportUser(MultiValueMap<String, String> headers, 
			RestTemplate restTemplate, String fullPath) 
	{
		User objectToPass = new User();
	    objectToPass.setName("Testy Tester");
	    objectToPass.setAddress("1313 Test Blvd");

	    HttpEntity<User> outReq = new HttpEntity<User>(objectToPass, headers);
	    String response = restTemplate.postForObject(fullPath, outReq, String.class);
	    
		return response;
	}

	private String reportStore(MultiValueMap<String, String> headers, 
			RestTemplate restTemplate, String fullPath) 
	{
		Store objectToPass = new Store();
	    objectToPass.setNumber(1);
	    objectToPass.setAddress("1313 Test Blvd");

	    HttpEntity<Store> outReq = new HttpEntity<Store>(objectToPass, headers);
	    String response = restTemplate.postForObject(fullPath, outReq, String.class);
	    
		return response;
	}

}

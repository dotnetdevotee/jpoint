package ru.jug.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import ru.jug.Store;

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
	    
	    Store objectToPass = new Store();
	    objectToPass.setNumber(1);
	    objectToPass.setAddress("1313 Test Blvd");

	    HttpEntity<Store> outReq = new HttpEntity<Store>(objectToPass, headers);
	    
	    //todo: Should limit to inbound POSTS and note appending of paths
	    String response = restTemplate.postForObject(serviceID + path, outReq, String.class);
	    
		return null;
	}

}

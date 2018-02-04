package ru.jug.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

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
	    //String path = request.getServletPath();
		String path = "/reporting";

	    SimpleRouteLocator routeLocator = 
	    		new SimpleRouteLocator(this.server.getServletPrefix(), zuulProperties);
	    
	    Route route = routeLocator.getMatchingRoute(path);
	    String serviceID = route.getLocation(); 
		
	    System.out.println("si: " + serviceID);
		
		System.out.println(
				"Request Method : " + request.getMethod() + " Request URL : " 
				+ request.getRequestURL().toString()); 

		return null;
	}

}

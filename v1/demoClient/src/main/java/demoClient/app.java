package demoClient;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ru.jug.Store;
import ru.jug.User;
import ru.jug.UserStore;
import ru.jug.UserStoreCollection;

@Controller
@EnableDiscoveryClient
@SpringBootApplication
public class app {
	
	static int storeNumber = 1;

	@Autowired app springApp;
	public static void main(String[] args) throws RestClientException, IOException {
		ApplicationContext ctx = 
	            new AnnotationConfigApplicationContext("demoClient");

        app main = ctx.getBean(app.class);
        
        main.springApp.addStore("10 S 120th, Chicago Ridge, IL 25894");
        main.springApp.addStore("8802 E 7th, Palos Ridge, IL 91802");
        main.springApp.addStore("1313 W Mockingbird, Munster, IN 11772");
        main.springApp.addStore("12 N Diversey, Chicago, IL 60604");
        main.springApp.addStore("999 Peachtree Street, Atlanta, GA 19002");
        
        main.springApp.addCustomer("Bob Dob", "14 Amareto, Keokuck, OH 92112");
        main.springApp.addCustomer("Sally Stora", "222 E Hillside, Kankakee, IL 21334");
        main.springApp.addCustomer("Vasya Tolstoy", "910 W Michigan Avenue, Chicago, IL 60332");
        main.springApp.addCustomer("Ingrid Patel", "91 S Bronx, New York 02213");
        main.springApp.addCustomer("Michael Nesmith", "4848 5th Avenue, Apt 3, New York 02214");
        
        UserStoreCollection us = main.springApp.getReportedStores();
	}
	
	private UserStoreCollection getReportedStores()
	{
		List<ServiceInstance> instances = discoveryClient.getInstances("ZUUL-SERVICE");
		ServiceInstance serviceInstance = instances.get(0);

		String baseUrl = serviceInstance.getUri().toString();
		System.out.println("baseUrl: " + baseUrl);
		
		String reportUrl = baseUrl + "/reporting/closestStoresForUsers";
	    
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<UserStoreCollection> response
		  = restTemplate.getForEntity(reportUrl, UserStoreCollection.class);
		
		return response.getBody();
	}

	private void addCustomer(String name, String address) 
	{	
		List<ServiceInstance> instances = discoveryClient.getInstances("ZUUL-SERVICE");
		ServiceInstance serviceInstance = instances.get(0);

		String baseUrl = serviceInstance.getUri().toString();
		System.out.println("baseUrl: " + baseUrl);
		
		String userUrl = baseUrl + "/users";
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	    headers.add("HeaderName", "value");
	    headers.add("Content-Type", "application/json");

	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	    
	    User objectToPass = new User();
	    objectToPass.setName(name);
	    objectToPass.setAddress(address);

	    HttpEntity<User> outReq = new HttpEntity<User>(objectToPass, headers);
	    
	    //todo: Should limit to inbound POSTS and note appending of paths
	    String response = restTemplate.postForObject(userUrl, outReq, String.class);

		System.out.println("Added company: " + response);
		
	}

	@Autowired
	private DiscoveryClient discoveryClient;
	
	private void addStore(String address)
	{
		List<ServiceInstance> instances = discoveryClient.getInstances("ZUUL-SERVICE");
		ServiceInstance serviceInstance = instances.get(0);

		String baseUrl = serviceInstance.getUri().toString();
		System.out.println("baseUrl: " + baseUrl);
		
		String storeUrl = baseUrl + "/stores";
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	    headers.add("HeaderName", "value");
	    headers.add("Content-Type", "application/json");

	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	    
	    Store objectToPass = new Store();
	    objectToPass.setNumber(storeNumber++);
	    objectToPass.setAddress(address);

	    HttpEntity<Store> outReq = new HttpEntity<Store>(objectToPass, headers);
	    
	    //todo: Should limit to inbound POSTS and note appending of paths
	    String response = restTemplate.postForObject(storeUrl, outReq, String.class);

		System.out.println("Added company: " + response);
	}
	
	/* public void getReportedStores() throws RestClientException, IOException {

		List<ServiceInstance> instances = discoveryClient.getInstances("ZUUL-SERVICE");
		ServiceInstance serviceInstance = instances.get(0);

		String baseUrl = serviceInstance.getUri().toString();
		System.out.println("baseUrl: " + baseUrl);

		baseUrl = baseUrl + "/reporting/stores";

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(baseUrl, HttpMethod.GET, getHeaders(), String.class);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		System.out.println(response.getBody());
	} */

	private static HttpEntity<?> getHeaders() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<>(headers);
	}

}

package ru.jug;

import java.util.ArrayList;
import java.util.Random;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	PersistenceMechanism pm = new PersistenceMechanism();
	
	@RequestMapping(value="/closestStoresForUsers", method = RequestMethod.GET)
	public ArrayList<UserStore> closestStoresForUsers()
	{
		ArrayList<UserStore> retVal = new ArrayList<UserStore>();
		ArrayList<User> users = pm.getUsers();
		
		for (User user : users) {
			UserStore us = new UserStore();
			us.setStore(getClosestStoreToAddress (user.getAddress()));
			us.setUser(user);
			retVal.add(us);
		}
		
		return retVal;
	}

	@RequestMapping(value = "/stores", method = RequestMethod.POST)
	public void addStore(@RequestBody Store store) 
	{
		pm.save(store);
	}
	
	@RequestMapping(value = "/stores", method = RequestMethod.GET)
	public ArrayList<Store> getStores()
	{
		return pm.getStores();	
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public void addStore(@RequestBody User user) 
	{
		pm.save(user);
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ArrayList<User> getUsers()
	{
		return pm.getUsers();	
	}
	
	private Store getClosestStoreToAddress(String address) {
		Random rn = new Random();
		ArrayList<Store> stores = pm.getStores();
		int index = rn.nextInt(stores.size());
		return stores.get(index);
	}

}

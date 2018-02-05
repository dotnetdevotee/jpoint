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
	
	@RequestMapping(value="/reporting/closestStoresForUsers", method = RequestMethod.GET)
	public UserStoreCollection closestStoresForUsers()
	{
		ArrayList<UserStore> retVal = new ArrayList<UserStore>();
		ArrayList<User> users = pm.getUsers();
		
		for (User user : users) {
			UserStore us = new UserStore();
			us.setStore(getClosestStoreToAddress (user.getAddress()));
			us.setUser(user);
			retVal.add(us);
		}
		
		UserStoreCollection usc = new UserStoreCollection();
		usc.setUserStores(retVal.toArray(new UserStore[0]));
		return usc;
	}

	@RequestMapping(value = "/reporting/stores", method = RequestMethod.POST)
	public void addStore(@RequestBody Store store) 
	{
		pm.save(store);
	}
	
	@RequestMapping(value = "/reporting/stores", method = RequestMethod.GET)
	public ArrayList<Store> getStores()
	{
		return pm.getStores();	
	}
	
	@RequestMapping(value = "/reporting/users", method = RequestMethod.POST)
	public void addStore(@RequestBody User user) 
	{
		pm.save(user);
	}
	
	@RequestMapping(value = "/reporting/users", method = RequestMethod.GET)
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

package ru.jug;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	PersistenceMechanism pm = new PersistenceMechanism();
	
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public void addStore(@RequestBody User user) 
	{
		pm.save(user);
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ArrayList<User> getStores()
	{
		return pm.get();	
	}

}

package ru.jug;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	PersistenceMechanism pm = new PersistenceMechanism();
	
	@RequestMapping(value = "/stores", method = RequestMethod.POST)
	public void addStore(@RequestBody Store store) 
	{
		pm.save(store);
	}
	
	@RequestMapping(value = "/stores", method = RequestMethod.GET)
	public ArrayList<Store> getStores()
	{
		return pm.get();	
	}

}

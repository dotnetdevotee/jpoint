package ru.jug;

import java.util.ArrayList;

public class PersistenceMechanism {
	
	static ArrayList<User> users = new ArrayList<User>();
	static ArrayList<Store> stores = new ArrayList<Store>();

	public void save(User store) 
	{
		PersistenceMechanism.users.add(store);
	}
	
	public ArrayList<User> getUsers()
	{
		return PersistenceMechanism.users;
	}

	public void save(Store store) 
	{
		PersistenceMechanism.stores.add(store);
	}
	
	public ArrayList<Store> getStores()
	{
		return PersistenceMechanism.stores;
	}

}

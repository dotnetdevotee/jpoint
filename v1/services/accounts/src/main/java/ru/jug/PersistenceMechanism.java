package ru.jug;

import java.util.ArrayList;

public class PersistenceMechanism {
	
	static ArrayList<User> users = new ArrayList<User>();

	public void save(User store) 
	{
		PersistenceMechanism.users.add(store);
	}
	
	public ArrayList<User> get()
	{
		return PersistenceMechanism.users;
	}

}

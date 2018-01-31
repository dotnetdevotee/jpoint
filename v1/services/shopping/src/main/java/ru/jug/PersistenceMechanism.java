package ru.jug;

import java.util.ArrayList;

public class PersistenceMechanism {
	
	static ArrayList<Store> stores = new ArrayList<Store>();

	public void save(Store store) 
	{
		PersistenceMechanism.stores.add(store);
	}
	
	public ArrayList<Store> get()
	{
		return PersistenceMechanism.stores;
	}

}

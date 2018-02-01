package ru.jug;

public class UserStore 
{
	private User user;
	private Store store;

	public UserStore() {

	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
}

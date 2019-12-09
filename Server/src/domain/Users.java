package domain;

import java.util.LinkedList;

public class Users {

	private LinkedList<User> usersList = new LinkedList<User>();
	
	public boolean isEmpty() {
		return usersList == null;
	}
	
	public void add(User user) {
		usersList.add(user);
	}
	
	public User find(String username) {
		for(User u: usersList) {
			if(u.getUsername().equals(username))
				return u;
		}
		return null;
	}
	
	public boolean verifyUser(String username, String password) {
		for(User u: usersList) {
			if(u.getUsername().equals(username) && u.getPassword().equals(password))
				return true;
		}
		return false;
	}
	
	public void preview() {
		for(User u: usersList) {
			System.out.println(u.getUsername() + " " + u.getPassword());
		}
	}

	public boolean isValid(Users users, String username, String password) {
		return false;
	}
	
	public boolean userNameIsValid(Users users, String username) {
		for(User u: users.getUsers()) {
			if(u.getUsername().equals(username))
				return false;
		}
		return true;
	}
	
	public LinkedList<User> getUsers() {
		return usersList;
	}
	
	
	
}

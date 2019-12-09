package validator.impl;

import domain.User;
import domain.Users;
import validator.Validator;

public class ValidatorImpl implements Validator{

	@Override
	public boolean isValid(Users users, String username, String password) {
		return false;
	}

	@Override
	public boolean userNameIsValid(Users users, String username) {
		for(User u: users.getUsers()) {
			if(u.getUsername().equals(username))
				return false;
		}
		return true;
	}


}

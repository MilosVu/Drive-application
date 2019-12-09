package validator;

import domain.Users;

public interface Validator {

	public boolean isValid(Users users, String username, String password);
	
	public boolean userNameIsValid(Users users, String username);
	
}

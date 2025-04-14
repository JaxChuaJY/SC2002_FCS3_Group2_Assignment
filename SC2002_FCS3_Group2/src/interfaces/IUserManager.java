package interfaces;

import java.util.List;

import user.User;

public interface IUserManager {

	String changePassword();

	boolean login();
	
	void logout();
	
	User searchUser_Type(Class<?> clazz, String name) throws Exception;
	
	void setList(List<User> list);
	
	User getcurrentUser();
	
	User findUserLogin(String ic, String pw);
	
	//REMOVE
	void printAllUser();

}
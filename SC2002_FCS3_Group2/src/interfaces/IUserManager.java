package interfaces;

import java.util.List;

import user.Applicant;
import user.User;

public interface IUserManager {

	void changePassword();

	boolean login();
	
	void logout();
	
	User searchUser_Type(Class<?> clazz, String name) throws Exception;
	
	void setList(List<User> list);
	
	User getcurrentUser();
	
	User findUserLogin(String ic, String pw);
	
	void reIntialise();
	
	//REMOVE
	void printAllUser();

	User getUser(String nric);

}
package interfaces;

import user.User;

public interface IUserManager {

	User loginUser();

	void changePassword(String nric, String password);

	User getUser(String nric);

}

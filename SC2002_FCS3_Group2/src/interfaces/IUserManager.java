package interfaces;

import user.User;

public interface IUserManager {

	void changePassword(String nric, String password);

	User getUser(String nric);

	User loginUser(String nric, String password);

}

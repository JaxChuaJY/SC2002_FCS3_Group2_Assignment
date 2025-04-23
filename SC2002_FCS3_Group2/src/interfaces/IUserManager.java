package interfaces;

import java.util.List;

import user.Applicant;
import user.User;

/**
 * Manages user authentication, session, and user data operations.
 */
public interface IUserManager {

	/**
     * Changes the password of the current user.
     */
	void changePassword();

	/**
     * Logs the user into the system.
     *
     * @return true if login successful, false otherwise
     */
	boolean login();
	
	/**
     * Logs the current user out of the system.
     */
	void logout();
	
	/**
     * Searches for a user by class type and identifier.
     *
     * @param clazz the class type of user (e.g., Applicant.class, HDBOfficer.class)
     * @param name the identifier (NRIC) of the user
     * @return the User object if found
     * @throws Exception if lookup fails or user not found
     */
	User searchUser_Type(Class<?> clazz, String name) throws Exception;
	
	/**
     * Sets the internal list of users.
     *
     * @param list the list of User objects to manage
     */
	void setList(List<User> list);
	
	/**
     * Retrieves the currently logged-in user.
     *
     * @return the User object representing the current user
     */
	User getcurrentUser();
	
	/**
     * Finds and returns a user matching the given credentials.
     *
     * @param ic the NRIC of the user
     * @param pw the password of the user
     * @return the matching User object
     */
	User findUserLogin(String ic, String pw);
	
	/**
     * Reinitializes the user manager state, e.g. clears session or reloads data.
     */
	void reIntialise();
	
	//REMOVE
	void printAllUser();

	/**
     * Retrieves a user by NRIC.
     *
     * @param nric the NRIC of the user
     * @return the matching User object
     */
	User getUser(String nric);

}
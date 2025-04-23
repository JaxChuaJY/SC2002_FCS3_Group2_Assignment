package user;

import enums.MaritalStatus;
import main.BTOManagementSystem;

/**
 * Abstract base class for all system users (Applicants, Officers, Managers).
 * <p>
 * Encapsulates common user attributes (name, NRIC, age, marital status, password)
 * and filter settings. Subclasses must implement role-specific menu behaviors.
 * </p>
 */
public abstract class User {
	/** The user's full name. */
	private String name;
    /** The user's login password. */
	private String password = "password";
    /** The user's age. */
	private int age;
    /** The user's marital status. */
	MaritalStatus maritalStatus;
    /** The user's NRIC identifier. */
	private String nric;
    /** User-specific filter settings for project viewing. */
	private FilterSettings filters;

	/**
     * Constructs a new User with specified credentials and filters.
     *
     * @param name           the user's name
     * @param nric           the user's NRIC identifier
     * @param age            the user's age
     * @param maritalstatus  the user's marital status
     * @param password       the user's login password
     */
	public User(String name, String nric, int age, MaritalStatus maritalstatus, String password) {
		this.name = name;
		this.nric = nric;
		this.age = age;
		this.maritalStatus = maritalstatus;
		this.password = password;
		this.filters = new FilterSettings();
	}

	/**
     * Displays the role-specific menu and delegates user actions.
     *
     * @param btoSys the BTOManagementSystem instance for delegating commands
     */
	public abstract void showMenu(BTOManagementSystem btoSys);

	/**
     * Returns a detailed string representation of the user.
     *
     * @return concatenated user attributes and type
     */
	public String toString() {
		return "Name: " + name + " NRIC: " + nric + " Age: " + age + " maritalStatus " + maritalStatus + " Password: "
				+ password + " | USER TYPE: " + this.getClass().toString();
	}

	//GETTER AND SETTERS

	/**
     * Retrieves the user's name.
     *
     * @return the name
     */
	public String getName() {
		return name;
	}

	/**
     * Updates the user's name.
     *
     * @param name the new name
     */
	public void setName(String name) {
		this.name = name;
	}

	/**
     * Retrieves the user's password.
     *
     * @return the password
     */
	public String getPassword() {
		return password;
	}

	/**
     * Updates the user's password.
     *
     * @param password the new password
     */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
     * Retrieves the user's age.
     *
     * @return the age
     */
	public int getAge() {
		return age;
	}

	/**
     * Updates the user's age.
     *
     * @param age the new age
     */
	public void setAge(int age) {
		this.age = age;
	}

	/**
     * Retrieves the user's marital status.
     *
     * @return the marital status
     */
	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}
	
	/**
     * Updates the user's marital status.
     *
     * @param maritalStatus the new marital status
     */
	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	/**
     * Updates marital status by string name (case-insensitive).
     *
     * @param input status name
     * @throws Exception if input does not match a valid status
     */
	public void setMaritalStatus(String input) throws Exception {
		for (MaritalStatus status : MaritalStatus.values()) {
			if (status.name().equalsIgnoreCase(input)) {
				this.maritalStatus = status;
				return;
			}
		}

		throw new Exception("Error in setting Marital Status");
	}

	/**
     * Retrieves the user's NRIC.
     *
     * @return the NRIC
     */
	public String getNric() {
		return nric;
	}

    /**
     * Updates the user's NRIC.
     *
     * @param nric the new NRIC
     */
	public void setNric(String nric) {
		this.nric = nric;
	}

	/**
     * Retrieves the user's filter settings.
     *
     * @return the FilterSettings instance
     */
	public FilterSettings getFilters() {
	    return this.filters;
	}

	/**
     * Replaces the user's filter settings.
     *
     * @param filters new FilterSettings
     */
	public void setFilters(FilterSettings filters) {
	    this.filters = filters;
	}

	
}

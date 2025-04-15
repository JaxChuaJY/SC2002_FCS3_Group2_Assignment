package user;

import enums.MaritalStatus;
import main.BTOManagementSystem;

public abstract class User {
	private String name;
	private String password = "password";
	private int age;
	MaritalStatus maritalStatus;
	private String nric;
	private FilterSettings filters;

	public User(String name, String nric, int age, MaritalStatus maritalstatus, String password) {
		this.name = name;
		this.nric = nric;
		this.age = age;
		this.maritalStatus = maritalstatus;
		this.password = password;
		this.filters = new FilterSettings();
	}

	public abstract void showMenu(BTOManagementSystem btoSys);

	public String toString() {
		return "Name: " + name + " NRIC: " + nric + " Age: " + age + " maritalStatus " + maritalStatus + " Password: "
				+ password + " | USER TYPE: " + this.getClass().toString();
	}

	//GETTER AND SETTERS

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}
	
	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public void setMaritalStatus(String input) throws Exception {
		for (MaritalStatus status : MaritalStatus.values()) {
			if (status.name().equalsIgnoreCase(input)) {
				this.maritalStatus = status;
				return;
			}
		}

		throw new Exception("Error in setting Marital Status");
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}
	
	public FilterSettings getFilters() {
	    return this.filters;
	}

	public void setFilters(FilterSettings filters) {
	    this.filters = filters;
	}

	
}

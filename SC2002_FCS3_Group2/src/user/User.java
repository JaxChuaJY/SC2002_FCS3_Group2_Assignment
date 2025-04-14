package user;

import main.BTOManagementSystem;

public abstract class User {
	private String name;
	private String password = "password";
	private int age;
	MaritalStatus maritalStatus;
	private String nric;

	public abstract void showMenu(BTOManagementSystem btoSys);
	

	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

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

	public String toString() {
		return "Name: " + name + " NRIC: " + nric + " Age: " + age + " maritalStatus " + maritalStatus + " Password: "
				+ password + " | USER TYPE: " + this.getClass().toString();
	}

	
}

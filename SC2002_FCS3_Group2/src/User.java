
public abstract class User {
	private String Name;
	private String Password = "password";	
	private int Age;
	private String NRIC;
	private MaritalStatus maritalStatus;
	//private SearchFilter filterSetting (?) - What are we implementing here? I'm a bit lost.
	
	public void changePassword(String newPassword) {
		this.Password = newPassword;
	}
	
	public boolean login(String nric, String password) {
		return this.NRIC == nric.toUpperCase() && this.Password == password;
	}
}

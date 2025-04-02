
public abstract class User {
	private String Name;
	private String Password;	
	private int Age;
	private String NRIC;
	private MaritalStatus maritalStatus;
	//private SearchFilter filterSetting (?) - What are we implementing here? I'm a bit lost.
	
	public User(String name, String password, int age, String nric, MaritalStatus maritalstatus) {
		this.Name = name;
		this.Password = password;
		this.Age = age;
		this.NRIC = nric;
		this.maritalStatus = maritalstatus;
	}
	
	public void changePassword(String newPassword) {
		this.Password = newPassword;
	}
	
	public boolean login(String nric, String password) {
		return this.NRIC == nric.toUpperCase() && this.Password == password;
	}

	public MaritalStatus getMaritalStatus(){
		return this.maritalStatus;
	}
}

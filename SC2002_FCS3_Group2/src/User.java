
public abstract class User {
	private String Name;
	private String Password;	
	private int Age;
	private String NRIC;
	private MaritalStatus maritalStatus;
	//private SearchFilter filterSetting (?) - What are we implementing here? I'm a bit lost.
	
	public User(String name, String nric, int age, MaritalStatus maritalstatus, String password) {
		this.Name = name;
		this.NRIC = nric;
		this.Age = age;
		this.maritalStatus = maritalstatus;
		this.Password = password;
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
	
	public String toString() {
		return Name; //To be Added
	}
}

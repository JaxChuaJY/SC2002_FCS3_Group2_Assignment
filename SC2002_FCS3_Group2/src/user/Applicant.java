package user;
import enums.MaritalStatus;

public class Applicant extends User{
	
	private Application application;
	
	public Applicant(String name, String nric, int age, 
			MaritalStatus maritalstatus, String password){
		super(name, nric, age, maritalstatus, password);
	}
}

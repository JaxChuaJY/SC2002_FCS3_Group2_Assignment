package user;
import application.Application;
import enums.ApplicationStatus;
import enums.FlatType;
import enums.MaritalStatus;
import interfaces.IApplicantAction;
import project.Project;

public class Applicant extends User implements IApplicantAction{
	
	private Application application;
	
	public Applicant(String name, String nric, int age, 
			MaritalStatus maritalstatus, String password){
		super(name, nric, age, maritalstatus, password);
	}
	
	@Override
	public void removeApplication() {
		this.application = null;
	}

	@Override
	public void applyForProject(Application a) {
		this.application = a;
		
	}

	@Override
	public void withdrawApplication() {
		this.application.setStatus(ApplicationStatus.WITHDRAW_REQUEST);
	}

	@Override
	public String viewApplicationStatus() {
		return this.application.getStatus().toString();	
	}
}

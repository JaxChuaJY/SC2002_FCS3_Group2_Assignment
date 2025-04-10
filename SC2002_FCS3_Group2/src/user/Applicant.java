package user;
import application.Application;
import application.ApplicationManager;
import enums.ApplicationStatus;
import enums.MaritalStatus;
import interfaces.IApplicantAction;

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
	public void withdrawApplication(ApplicationManager aM) {
		aM.getApplicationList().values().forEach(set -> 
        set.stream()
        	.filter(application -> application.getApplicant().equals(this))
        	.forEach(application -> application.setStatus(ApplicationStatus.WITHDRAW_REQUEST)));
		System.out.println("Application withdrawn request successful.");
		this.application.setStatus(ApplicationStatus.WITHDRAW_REQUEST);
	}

	@Override
	public String viewApplicationStatus() {
		return this.application.getStatus().toString();	
	}
}

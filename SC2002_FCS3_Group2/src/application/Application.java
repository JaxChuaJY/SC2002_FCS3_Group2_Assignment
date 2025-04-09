package application;
import enums.ApplicationStatus;
import enums.FlatType;
import project.Project;
import user.Applicant;

public class Application {
	
	private Applicant applicant;
	private Project project;
	private FlatType flatType;
	private ApplicationStatus status;
	
	public Application(Applicant applicant, Project project, FlatType flatType, ApplicationStatus status) {
		this.applicant = applicant;
		this.project = project;
		this.flatType = flatType;
		this.status = status;
	}
	
	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}
	
}

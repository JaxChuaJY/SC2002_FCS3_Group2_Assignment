package registration;

import java.util.List;

import enums.ApplicationStatus;
import project.Project;
import user.HDBOfficer;

public class RegistrationForm {
	private HDBOfficer registeredBy;
	private Project project;
	private ApplicationStatus status;
	
	public RegistrationForm(Project project, HDBOfficer user) {
		// TODO Auto-generated constructor stub
		this.project = project;
		registeredBy = user;
		status = ApplicationStatus.WAITING;
	}
	
	public RegistrationForm() {
		// TODO Auto-generated constructor stub
	}

	public void approveStatus() {
		status = ApplicationStatus.APPROVED;
	}
	
	public void rejectStatus() {
		status = ApplicationStatus.REJECTED;
	}
	
	public String toString() {
		return "Registration Form of " + registeredBy.getName() +
				" for Project: " + project.getProjectName() +
				" | Status: " + status;
		
	}

	public HDBOfficer getRegisteredBy() {
		return registeredBy;
	}

	public void setRegisteredBy(HDBOfficer registeredBy) {
		this.registeredBy = registeredBy;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}
	
	public void setStatus(String s) throws Exception {
		for (ApplicationStatus status : ApplicationStatus.values()) {
			if (status.name().equalsIgnoreCase(s)) {
				this.status = status;
				return;
			}
		}
		
		throw new Exception("Error in setting Status of Form");
	}
	
	
}

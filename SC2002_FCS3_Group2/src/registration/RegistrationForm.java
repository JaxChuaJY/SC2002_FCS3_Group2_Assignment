package registration;

import java.util.List;

import enums.ApplicationStatus;
import project.Project;
import user.HDBOfficer;

/**
 * Represents an HDB Officer's registration request for a BTO project.
 * <p>
 * Captures the applicant officer, target project, timestamps, and approval status.
 * </p>
 */
public class RegistrationForm {

	/** Officer who submitted this registration. */
	private HDBOfficer registeredBy;
	
    /** Project for which registration is requested. */
	private Project project;
	
    /** Current approval status of the registration. */
	private ApplicationStatus status;
	
	/**
     * Constructs a new RegistrationForm for the given project and officer,
     * generating a default formId based on officer name and timestamp.
     *
     * @param project      the project to register for
     * @param hdbOfficer   the HDBOfficer submitting the registration
     */
	public RegistrationForm(Project project, HDBOfficer hdbOfficer) {
		// TODO Auto-generated constructor stub
		this.project = project;
		registeredBy = hdbOfficer;
		status = ApplicationStatus.PENDING;
	}
	
	
	public RegistrationForm() {
		// TODO Auto-generated constructor stub
	}

	/**
     * Approves this registration form, updating status.
     */
	public void approveStatus() {
		status = ApplicationStatus.SUCCESSFUL;
	}
	
	/**
     * Approves this registration form, updating status.
     */
	public void rejectStatus() {
		status = ApplicationStatus.UNSUCCESSFUL;
	}
	
	/**
     * Returns a string representation of this form.
     *
     * @return detailed form information
     */
	public String toString() {
		return "Registration Form of " + registeredBy.getName() +
				" for Project: " + project.getProjectName() +
				" | Status: " + status;
		
	}

	/**
     * @return the officer who submitted this form
     */
	public HDBOfficer getRegisteredBy() {
		return registeredBy;
	}

	/**
     * @return the officer who submitted this form
     */
	public void setRegisteredBy(HDBOfficer registeredBy) {
		this.registeredBy = registeredBy;
	}

	/**
     * @return the project this form applies to
     */
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	/**
     * Gets the form's current status.
     *
     * @return the ApplicationStatus of this form
     */
	public ApplicationStatus getStatus() {
		return status;
	}

	/**
     * @return the unique form identifier
     */
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

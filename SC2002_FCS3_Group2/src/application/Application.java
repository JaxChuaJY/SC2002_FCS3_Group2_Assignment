package application;

import enums.ApplicationStatus;
import enums.FlatType;
import project.Project;
import user.Applicant;

/**
 * Represents an application for a BTO project by an applicant.
 * Stores details such as the applicant, project, flat type, and application status.
 */
public class Application {
	
	private Applicant applicant;
	private Project project;
	private FlatType flatType;
	private ApplicationStatus status;
	
	/**
     * Constructs a new Application with the specified details.
     *
     * @param applicant the applicant submitting the application
     * @param project   the BTO project being applied for
     * @param flatType  the type of flat being applied for
     * @param status    the initial status of the application
     * @throws IllegalArgumentException if any parameter is null
     */
    public Application(Applicant applicant, Project project, FlatType flatType, ApplicationStatus status) {
        this.applicant = applicant;
        this.project = project;
        this.flatType = flatType;
        this.status = status;
    }
    
    /**
     * Sets the status of the application.
     *
     * @param status the new status of the application
     * @throws IllegalArgumentException if the status is null
     */
    public void setStatus(ApplicationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        this.status = status;
    }
	
	public void viewProject() {
		System.out.print(project);
	}
	
	/**
     * Gets the project associated with this application.
     *
     * @return the project
     */
	public Project getProject() {
		return this.project;
	}
	
	/**
     * Gets the applicant who submitted this application.
     *
     * @return the applicant
     */
	public Applicant getApplicant() {
		return this.applicant;
	}
	
	/**
     * Gets the status of this application.
     *
     * @return the application status
     */
	public ApplicationStatus getStatus() {
		return this.status;
	}
	
	/**
     * Returns a string representation of the application, including the applicant's NRIC,
     * project name, flat type, and status.
     *
     * @return a string representation of the application
     */
	public String toString() {
		return "===========================\n" +
               "NRIC: " + applicant.getNric() + "\n" +
               "Project: " + project.getProjectName() + "\n" +
               "Flat Type: " + flatType.toString() + "\n" +
               "Status: " + status.toString() + "\n" +
               "===========================";
	}
}
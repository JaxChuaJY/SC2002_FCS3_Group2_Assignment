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
	private ApplicationStatus previousStatus;
	
	/**
     * Constructs a new Application with the specified details.
     *
     * @param applicant the applicant submitting the application
     * @param project   the BTO project being applied for
     * @param flatType  the type of flat being applied for
     * @param status    the initial status of the application
	 * @param prevStatus 
     * @throws IllegalArgumentException if any parameter is null
     */
    public Application(Applicant applicant, Project project, FlatType flatType, ApplicationStatus status, ApplicationStatus prevStatus) {
        this.applicant = applicant;
        this.project = project;
        this.flatType = flatType;
        this.status = status;
    	this.previousStatus = prevStatus;
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
        this.previousStatus = this.status;
        this.status = status;
    }
    
    /**
     *Reverts status back to previous saved status
     */
    public void revertStatus() {
        this.status = this.previousStatus;
    }
    
    /**
     *Get Previous Status
     *
     *@return the previous status
     */
    public ApplicationStatus getPreviousStatus() {
        return this.previousStatus;
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
     *Get the flatType associated with this application
     *
     *@return the application's flat type
     */
	public FlatType getFlatType() {
		return this.flatType;
	}
	/**
     * Returns a string representation of the application, including the applicant's NRIC,
     * project name, flat type, and status.
     *
     * @return a string representation of the application
     */
	public String toString() { return "NRIC: " + applicant.getNric() + " Project: " + project.getProjectName() + " Flat Type: " + flatType + " Status: " + status; }
	
	/**
     * Returns a file format string representation of the application, including the applicant's NRIC,
     * project name, flat type, and status.
     *
     * @return a file format string representation of the application
     */
	public String fileFormat() {
		return project.getProjectName() + "," + applicant.getNric() + "," + flatType.toDisplayString() + "," + status.toString() + "," + previousStatus.toString();
	}

	
}
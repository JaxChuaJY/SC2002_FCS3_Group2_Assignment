package interfaces;

import application.Application;
import application.ApplicationManager;

/**
 * Defines actions that an applicant can perform in the BTO system.
 * Includes viewing application status, creating, removing, or withdrawing applications.
 */
public interface IApplicantAction {
	
	/**
     * Retrieves the current status of the applicant's BTO application.
     *
     * @return a string representing the application status
     */
	String viewApplicationStatus();

	/**
     * Submits a new application for a BTO project.
     *
     * @param a the Application object containing applicant, project, and flat type
     */
	void applyForProject(Application a);


    /**
     * Removes the applicant's existing application.
     */
	void removeApplication();
	
	/** 
	 * Withdraws the applicant's application via the ApplicationManager.
     *
     * @param aM the ApplicationManager handling the withdrawal operation
     */
	void withdrawApplication(ApplicationManager aM);
}

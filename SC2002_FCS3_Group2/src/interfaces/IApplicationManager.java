package interfaces;

import project.Project;
import user.User;
import application.Application;
import enums.FlatType;
import enums.MaritalStatus;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;


/**
 * Manages BTO application operations, including loading/saving,
 * creating, approving, rejecting, booking, receipt handling, and reporting.
 */

public interface IApplicationManager {
   
    /**
     * Loads all applications from the CSV data source.
     */
    void loadApplicationFromCSV();

    
    /**
     * Saves current applications to the CSV data source.
     */
    void saveApplicationtoCSV();
    
    /**
     * Creates a new BTO application.
     *
     * @param user the User submitting the application
     * @param project the Project being applied for
     * @param flatType the type of flat requested
     */
    void createApplication(User user, Project project, FlatType flatType);

    /**
     * Retrieves all applications for a given project.
     *
     * @param project the Project to query
     * @return a list of Application objects
     */
    List<Application> getApplicationsForProject(Project project);
    
    /**
     * Retrieves the application for a specific user.
     *
     * @param user the User whose application is requested
     * @return the Application object, or null if none exists
     */
    Application getApplicationsForUser(User user);

    /**
     * Approves a BTO application.
     *
     * @param application the Application to approve
     */    
    void approveApplication(Application application);
    
    /**
     * Rejects a BTO application.
     *
     * @param application the Application to reject
     */
    void rejectApplication(Application application);

    /**
     * Requests withdrawal of a BTO application.
     *
     * @param application the Application to withdraw
     */
    void requestWithdrawal(Application application);

    /**
     * Approves a withdrawal request for an application.
     *
     * @param application the Application with withdrawal request
     */
    void approveWithdrawal(Application application);
    
    /**
     * Rejects a withdrawal request for an application.
     *
     * @param application the Application with withdrawal request
     */    
    void rejectWithdrawal(Application application);

    /**
     * Books a flat for a successful application.
     *
     * @param application the Application being booked
     */ 
    void bookFlat(Application application);
    
    /**
     * Generates a receipt for a booked application.
     *
     * @param application the Application for which to write the receipt
     */
    void writeReceipt(Application application);

    /**
     * Displays or logs the receipt for a booked application.
     *
     * @param application the Application whose receipt is viewed
     */
	void viewReceipt(Application application);
	
    /**
     * Generates a report of applicant choices filtered by criteria.
     *
     * @param maritalStatus optional filter for marital status
     * @param flatType optional filter for flat type
     * @param minAge optional minimum age filter
     * @param maxAge optional maximum age filter
     * @param projectName optional project name filter
     * @return list of report strings matching the criteria
     */
	List<String> generateReport(Optional<MaritalStatus> maritalStatus, Optional<FlatType> flatType, OptionalInt minAge, OptionalInt maxAge, Optional<String> projectName);
}
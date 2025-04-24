package interfaces;

import enquiry.Enquiry;
import java.util.List;
import project.Project;
import user.User;

/**
 * Manages user enquiries for BTO projects, including persistence and retrieval.
 * <p>
 * Supports loading from CSV, exporting to CSV, adding new enquiries,
 * querying by sender or project, editing messages, and replying.
 * </p>
 */
public interface IEnquiryManager {
	
	/**
     * Adds a new enquiry to the in-memory list.
     *
     * @param sender  the User who submits the enquiry
     * @param project the Project being enquired about
     * @param message the enquiry message
     */
    void addEnquiry(User sender, Project project, String message);

    /**
     * Edits the message of an enquiry if it has not been replied.
     *
     * @param id         the enquiry ID
     * @param newMessage the new message content
     */
    void editEnquiry(int id, String newMessage);

    /**
     * Sets a reply for an enquiry by ID.
     *
     * @param id    the enquiry ID
     * @param reply the reply message to record
     */
    void replyToEnquiry(int id, String reply);

    /**
     * Retrieves all enquiries (replied or not) for a given project.
     *
     * @param project the Project to filter by
     * @return list of Enquiry for that project
     */
    List<Enquiry> getEnquiryNOReply(Project project);

    /**
     * Retrieves enquiries sent by a specific user.
     *
     * @param user the sender User
     * @return list of matching Enquiry objects
     */
    List<Enquiry> getEnquiry_filterSend(User user);

    /**
     * Retrieves the first enquiry for a specific project.
     *
     * @param project the Project to search
     * @return the Enquiry found or null if none
     */
    Enquiry getEnquiryByProject(Project project);

    /**
     * Prints all enquiries and their replies to console.
     */
    void printAll();

    /**
     * Checks if an enquiry is deletable (not replied).
     *
     * @param  enquiry to check
     * @return status
     */
    boolean deleteCheck(Enquiry enquiry);

    /**
     * Deletes an enquiry.
     *
     * @param  enquiry to delete
     */
    void deleteEnquiry(Enquiry enquiry);
    
    /**
     * Adds an existing Enquiry instance to the list.
     *
     * @param enquiry the Enquiry to add
     */
    void createEnquiryList(Enquiry enquiry);
    
    /**	
     * Loads enquiries from a CSV file, creating Enquiry instances.
     * <p>
     * If the file does not exist, creates a new one with a header.
     * </p>
     *
     * @param filename       path to the CSV file
     * @param userManager    for resolving sender User objects
     * @param projectManager for resolving Project objects
     */
    void loadFromCSV(String filename, IUserManager userManager, IProjectManager projectManager);
}

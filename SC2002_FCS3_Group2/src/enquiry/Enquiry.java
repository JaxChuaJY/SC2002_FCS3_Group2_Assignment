package enquiry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import project.Project;
import user.User;

/**
 * Represents a user enquiry about a specific BTO project.
 * <p>
 * Captures sender, project referenced, message content, reply status,
 * timestamps, and unique enquiry ID.
 * </p>
 */
public class Enquiry {
    /** Counter to generate unique enquiry IDs. */
    private static int counter = 1;
    /** The user who submitted this enquiry. */
    private User sender;
    /** The project this enquiry pertains to. */
    private Project project;
    /** The enquiry message content. */
    private String message;
    /** The reply message content, if any. */
    private String reply;
    /** Unique identifier for this enquiry. */
    private int enquiryId;
    /** Date when the enquiry was created. */
    private LocalDate dateCreated;
    /** Date when the enquiry was replied to. */
    private LocalDate dateReplied;
    /** Flag indicating whether this enquiry has been replied. */
    private boolean replied;
    
    /** Formatter for dates in dd/MM/yyyy format. */
    private static final DateTimeFormatter a = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Constructs a new Enquiry instance.
     *
     * @param sender  the user who creates the enquiry
     * @param project the project being enquired about
     * @param message the enquiry message content
     */
    public Enquiry (User sender, Project project, String message) {
        this.sender = sender;
        this.project = project;
        this.message = message; 
        this.reply = "None.";
        this.replied = false;
        this.dateCreated = LocalDate.now();
        this.enquiryId = counter++;
    }
    
    /**
     * Sets the reply message and marks this enquiry as replied.
     *
     * @param reply the reply content to record
     */
    public void setReply(String reply) {
        this.reply = reply;
        this.replied = true;
        this.dateReplied = LocalDate.now();
    }

    /**
     * Updates the enquiry message content.
     *
     * @param message the new message content
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Retrieves the formatted reply date.
     *
     * @return the date replied as a string, or empty if not replied
     */
    public String getDateReplied() { return (dateReplied == null) ? "" : dateReplied.format(a); }
   
    /**
     * Manually sets the creation date of this enquiry.
     *
     * @param dateCreated the creation date to assign
     */ 
    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Manually sets the reply date of this enquiry.
     *
     * @param dateReplied the reply date to assign
     */
    public void setDateReplied(LocalDate dateReplied) {
        this.dateReplied = dateReplied;
    }
    /** @return the user who submitted this enquiry */
    public User getSender() { return sender; }
     /** @return the project referenced in this enquiry */
    public Project getProject() { return project; }
    /** @return the enquiry message content */
    public String getMessage() { return message; }
    /** @return the unique enquiry ID */
    public int getEnquiryId() { return enquiryId; }
    /** @return true if this enquiry has been replied, false otherwise */
    public boolean isReplied() { return replied; }
    /** Retrieves the formatted creation date.
     * @return the creation date as a string */
    public String getDateCreated() { return dateCreated.format(a); }
    /** @return the reply content, or a default message if none */
    public String getReply() { return reply; }

    /**
     * Returns a detailed string representation of this enquiry.
     *
     * @return formatted enquiry details including sender, project, and dates
     */
    @Override
    public String toString() {
        return "Enquiry ID: " + enquiryId +
        "\nSender: " + sender.getName() +
        "\nProject: " + project.getProjectName() +
        "\nMessage: " + message +
        "\nReply: " + (replied ? reply : "No reply yet") +
        "\nDate Created: " + dateCreated +
        (replied ? "\nDate Replied: " + dateReplied : "");
    }
}
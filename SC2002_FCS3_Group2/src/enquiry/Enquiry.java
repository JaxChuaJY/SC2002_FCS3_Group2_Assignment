package enquiry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import project.Project;
import user.User;

public class Enquiry {
    private static int counter = 1;
    private User sender;
    private Project project;
    private String message;
    private String reply;
    private int enquiryId;
    private LocalDate dateCreated;
    private LocalDate dateReplied;
    private boolean replied;
    
    private static final DateTimeFormatter a = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Enquiry (User sender, Project project, String message) {
        this.sender = sender;
        this.project = project;
        this.message = message; 
        this.reply = "None.";
        this.replied = false;
        this.dateCreated = LocalDate.now();
        this.enquiryId = counter++;
    }

    public void setReply(String reply) {
        this.reply = reply;
        this.replied = true;
        this.dateReplied = LocalDate.now();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() { return sender; }
    public Project getProject() { return project; }
    public String getMessage() { return message; }
    public int getEnquiryId() { return enquiryId; }
    public boolean isReplied() { return replied; }
    public String getDateCreated() { return dateCreated.format(a); }
    public String getDateReplied() {return dateReplied.format(a); }
    public String getReply() { return reply; }

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
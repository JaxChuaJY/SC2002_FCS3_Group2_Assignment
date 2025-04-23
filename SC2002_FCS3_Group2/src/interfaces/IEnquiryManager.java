package interfaces;

import enquiry.Enquiry;
import java.util.List;
import project.Project;
import user.User;

public interface IEnquiryManager {
    void addEnquiry(User sender, Project project, String message);

    void editEnquiry(int id, String newMessage);

    void replyToEnquiry(int id, String reply);

    List<Enquiry> getEnquiryNOReply(Project project);

    List<Enquiry> getEnquiry_filterSend(User user);

    Enquiry getEnquiryByProject(Project project);

    void printAll();

    boolean deleteCheck(int id);

    void createEnquiryList(Enquiry enquiry);

    void loadFromCSV(String filename, IUserManager userManager, IProjectManager projectManager);
}
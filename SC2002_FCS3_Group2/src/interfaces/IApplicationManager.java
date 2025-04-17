package interfaces;

import project.Project;
import user.User;
import application.Application;
import enums.FlatType;

import java.util.List;

public interface IApplicationManager {
    // Load applications from a data source (e.g., CSV)
    void loadApplicationFromCSV();

    // Save applications to a data source (e.g., CSV)
    void saveApplicationtoCSV();
    
    // Create a new application for a user and project
    void createApplication(User user, Project project, FlatType flatType);

    // Retrieve all applications for a specific project
    List<Application> getApplicationsForProject(Project project);

    // Retrieve applications for a specific user
    Application getApplicationsForUser(User user);

    // Approve an application for a user and project
    void approveApplication(Application application);
    
    // Reject an application for a user and project
    void rejectApplication(Application application);

    // Request withdrawal of an application for a user
    void requestWithdrawal(Application application);

    // Approve a withdrawal request for a user and project
    void approveWithdrawal(Application application);
    
    // Reject a withdrawal request for a user and project
    void rejectWithdrawal(Application application);

    void bookFlat(Application application);
    
    void writeReceipt(Application application);
}
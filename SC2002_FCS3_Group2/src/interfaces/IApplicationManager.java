package interfaces;

import project.Project;
import user.Applicant;
import user.HDBManager;
import user.HDBOfficer;
import user.User;
import application.Application;
import enums.FlatType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IApplicationManager {
    // Load applications from a data source (e.g., CSV)
    void loadApplicationFromCSV();

    // Create a new application for a user and project
    void createApplication(User user, Project project, FlatType flatType);

    // Retrieve all applications for a specific project
    List<Application> getApplicationsForProject(Project project);

    // Retrieve applications for a specific user
    Application getApplicationsForUser(User user);

    // Approve an application for a user and project
    void approveApplication(User user);

    // Request withdrawal of an application for a user
    boolean requestWithdrawal(User user);

    // Approve a withdrawal request for a user and project
    void approveWithdrawal(User user);

    // Get the full application list (for internal use or admin purposes)
    Map<String, Set<Application>> getApplicationList();

}
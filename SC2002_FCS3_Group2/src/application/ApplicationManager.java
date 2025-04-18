package application;

import enums.FlatType;
import interfaces.*;
import project.Project;
import user.Applicant;
import user.User;

import java.util.List;

public class ApplicationManager implements IApplicationManager {
    private IApplicationService applicationService;
    private IApplicationFileHandler applicationFileHandler;
    private IReceiptManager receiptManager;

    public ApplicationManager(IApplicationService applicationService, IApplicationFileHandler applicationFileHandler,
                              IReceiptManager receiptManager) {
        this.applicationService = applicationService;
        this.applicationFileHandler = applicationFileHandler;
        this.receiptManager = receiptManager;
        
    }

    @Override
    public void loadApplicationFromCSV() {
        applicationFileHandler.loadApplications();
    }

    @Override
    public void saveApplicationtoCSV() {
        applicationFileHandler.saveApplications();
    }

    @Override
    public void createApplication(User user, Project project, FlatType flatType) {
        if (!(user instanceof Applicant)) {
            throw new IllegalArgumentException("User must be an Applicant.");
        }
        Applicant applicant = (Applicant) user;
        applicationService.createApplication(applicant, project, flatType);
    }

    @Override
    public Application getApplicationsForUser(User user) {
        if (!(user instanceof Applicant)) {
            throw new IllegalArgumentException("User must be an Applicant.");
        }
        Applicant applicant = (Applicant) user;
        return applicationService.getApplicationForApplicant(applicant);
    }

    @Override
    public List<Application> getApplicationsForProject(Project project) {
        return applicationService.getApplicationsForProject(project);
    }

    @Override
    public void approveApplication(Application application) {
        applicationService.approveApplication(application);
    }

    @Override
    public void rejectApplication(Application application) {
        applicationService.rejectApplication(application);
    }

    @Override
    public void requestWithdrawal(Application application) {
        applicationService.requestWithdrawal(application);
    }

    @Override
    public void approveWithdrawal(Application application) {
        applicationService.approveWithdrawal(application);
    }

    @Override
    public void rejectWithdrawal(Application application) {
        applicationService.rejectWithdrawal(application);
    }

    @Override
    public void bookFlat(Application application) {
        applicationService.bookFlat(application);
    }

    @Override
    public void writeReceipt(Application application) {
        receiptManager.writeReceipt(application);
    }

    @Override
    public void viewReceipt(Application application) {
        receiptManager.viewReceipt(application);
    }
}
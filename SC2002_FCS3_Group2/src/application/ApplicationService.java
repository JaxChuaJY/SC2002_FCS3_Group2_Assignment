package application;

import enums.ApplicationStatus;
import enums.FlatType;
import interfaces.IApplicationService;
import interfaces.IProjectManager;
import interfaces.IUserManager;
import project.Project;
import user.Applicant;
import user.HDBOfficer;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ApplicationService implements IApplicationService {
    private Map<String, Set<Application>> applicationList;
    private IProjectManager projectManager;
    private IUserManager userManager;

    public ApplicationService(IProjectManager projectManager,IUserManager userManager) {
        this.applicationList = new HashMap<>();
		this.projectManager = projectManager;
		this.userManager = userManager;
    }

    @Override
    public void createApplication(Applicant applicant, Project project, FlatType flatType) {
        if (applicant == null) {
            throw new IllegalArgumentException("Applicant cannot be null.");
        }
        if (project == null || LocalDate.now().isAfter(project.getClosingDate()) || !project.getVisibility()) {
            throw new IllegalArgumentException("Invalid project.");
        }
        if (applicant instanceof HDBOfficer officer && officer.getProjects().contains(project)) {
            throw new IllegalArgumentException("Cannot apply for a project you are managing.");
        }

        // Clear previous application if withdrawn or unsuccessful
        if (applicant.getApplication() != null &&
                (applicant.getApplication().getStatus() == ApplicationStatus.WITHDRAWN ||
                        applicant.getApplication().getStatus() == ApplicationStatus.UNSUCCESSFUL)) {
            Set<Application> projectApps = applicationList.get(applicant.getApplication().getProject().getProjectName());
            if (projectApps != null) {
                projectApps.remove(applicant.getApplication());
            }
        }

        Application application = new Application(applicant, project, flatType, ApplicationStatus.PENDING, ApplicationStatus.PENDING);
        applicationList.computeIfAbsent(project.getProjectName(), k -> new HashSet<>()).add(application);
        applicant.setApplication(application);
    }

    @Override
    public Application getApplicationForApplicant(Applicant applicant) {
        return applicationList.values().stream()
                .flatMap(Set::stream)
                .filter(a -> a.getApplicant().getNric().equals(applicant.getNric()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Application> getApplicationsForProject(Project project) {
        return applicationList.values().stream()
                .flatMap(Set::stream)
                .filter(a -> a.getProject().equals(project))
                .collect(Collectors.toList());
    }

    @Override
    public void approveApplication(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null.");
        }
        boolean reserved =  projectManager.updateFlatRooms(application.getProject(), application.getFlatType(), -1);
        if (reserved) {
            application.setStatus(ApplicationStatus.SUCCESSFUL);
            System.out.println("[APPROVED] Flat successfully reserved for " + application.getApplicant().getName());
        } else {
        	System.out.println("[FAILED] Flat type " + application.getFlatType() + " is no longer available.");
        }
    }

    @Override
    public void rejectApplication(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null.");
        }
        application.setStatus(ApplicationStatus.UNSUCCESSFUL);
    }

    @Override
    public void requestWithdrawal(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null.");
        }
        application.setStatus(ApplicationStatus.WITHDRAW_REQUEST);
    }

    @Override
    public void approveWithdrawal(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null.");
        }
        if (application.getPreviousStatus() == ApplicationStatus.BOOKED) {
        	projectManager.updateFlatRooms(application.getProject(), application.getFlatType(), 1);
        }
        application.setStatus(ApplicationStatus.WITHDRAWN);
    }

    @Override
    public void rejectWithdrawal(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null.");
        }
        application.revertStatus();
    }

    @Override
    public void bookFlat(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null.");
        }
        application.setStatus(ApplicationStatus.BOOKED);
    }

    @Override
    public Map<String, Set<Application>> getApplicationList() {
        return applicationList;
    }
    
    @Override
    public void addApplication(Application application) {
        applicationList.computeIfAbsent(application.getProject().getProjectName(), k -> new HashSet<>()).add(application);
    }
}
package application;

import enums.ApplicationStatus;
import enums.FlatType;
import interfaces.IApplicationFileHandler;
import interfaces.IApplicationService;
import interfaces.IFileHandler;
import interfaces.IProjectManager;
import interfaces.IUserManager;
import project.Project;
import user.Applicant;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationFileHandler implements IApplicationFileHandler {
    private static String APPLICATIONS_FILE = "data/Applications.csv";
    private IFileHandler fileHandler;
    private IProjectManager projectManager;
    private IUserManager userManager;
    private IApplicationService applicationService;

    public ApplicationFileHandler(IFileHandler fileHandler,
                                  IProjectManager projectManager, IUserManager userManager, IApplicationService applicationService) {
        this.fileHandler = fileHandler;
        this.projectManager = projectManager;
        this.userManager = userManager;
        this.applicationService = applicationService;
    }

	@Override
	public void loadApplications() {
	    try {
			fileHandler.readFromFile(APPLICATIONS_FILE).stream()
	        .skip(1) // Ignore header row in CSV
	        .map(entry -> entry.split(","))
	        .map(this::createApplicationFromFile)
	        .filter(application -> application != null) // Potentially handle broken or malformed rows here
	        .forEach(application -> {
	        	applicationService.addApplication(application);
	            User user = userManager.getUser(application.getApplicant().getNric());
	            if (user instanceof Applicant applicant) {
	                applicant.setApplication(application);
	            }
	        });
	    } catch (Exception e) {
	        System.err.println("Failed to load applications: " + e.getMessage());
	    }
	}
    
	private Application createApplicationFromFile(String[] values) {
		String projectName = values[0];
		String nric = values[1];
		FlatType flatType = FlatType.fromString(values[2]);
		ApplicationStatus appStatus = ApplicationStatus.valueOf(values[3].toUpperCase());
		ApplicationStatus prevStatus = ApplicationStatus.valueOf(values[4].toUpperCase());

		Project project = projectManager.getProject(projectName);
		Applicant user = (Applicant) userManager.getUser(nric);

		return new Application(user, project, flatType, appStatus, prevStatus);
	}
    
	@Override
	public void saveApplications() {
		try {
			List<String> fileFormat = applicationService.getApplicationList().values().stream()
					.flatMap(Set::stream)
					.map(data -> data.fileFormat())
					.collect(Collectors.toCollection(ArrayList::new));
	
			fileFormat.add(0, "Project Name,Applicant,FlatType,ApplicationStatus,PreviousStatus");
			fileHandler.writeToFile(APPLICATIONS_FILE,fileFormat);
		} catch (Exception e) {
	        System.err.println("Failed to load applications: " + e.getMessage());
	    }
	}
}
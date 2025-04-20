package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import enums.ApplicationStatus;
import enums.FlatType;
import interfaces.IApplicationManager;
import interfaces.IFileHandler;
import interfaces.IProjectManager;
import interfaces.IUserManager;
import project.Project;
import user.Applicant;
import user.HDBManager;
import user.HDBOfficer;
import user.User;

/**
 * Manages applications for BTO projects, including creating, retrieving,
 * approving, and withdrawing applications. Applications are stored in a map
 * where the key is the project name and the value is a set of applications for
 * that project. This class interacts with {@link IProjectManager} and
 * {@link IUserManager} to access project and user data.
 */
public class ApplicationManager implements IApplicationManager {

	private Map<String, Set<Application>> applicationList;

	private final IProjectManager projectManager;
	private final IUserManager userManager;
	private final IFileHandler fileHandler;

	private static final String APPLICATIONS_FILE = "data/ApplicationList.csv";

	/**
	 * Constructs an ApplicationManager with the necessary dependencies.
	 *
	 * @param projectManager the project manager to access project data
	 * @param userManager    the user manager to access user data
	 * @param fileHandler    the file handler to read application data from CSV
	 */
	public ApplicationManager(IProjectManager projectManager, IUserManager userManager, IFileHandler fH) {
		this.projectManager = projectManager;
		this.userManager = userManager;
		this.fileHandler = fH;
		this.applicationList = new HashMap<>();
		loadApplicationFromCSV();
	}

	/**
	 * Loads applications from a CSV file into the application list. The CSV file is
	 * expected to have columns: projectName, nric, flatType, status.
	 */
	@Override
	public void loadApplicationFromCSV() {
		fileHandler.readFromFile(APPLICATIONS_FILE).stream()
        .skip(1)
        .map(entry -> entry.split(","))
        .map(this::createApplicationFromFile)
        .filter(application -> application != null)
        .forEach(application -> {
            applicationList.computeIfAbsent(application.getProject().getProjectName(), k -> new HashSet<>())
                    .add(application);
            User user = userManager.getUser(application.getApplicant().getNric());
            if (user instanceof Applicant applicant) {
                applicant.setApplication(application);
            }
        });
	}

	/**
	 * Saves the current application data to a CSV file.
	 * The CSV file will include the project name, applicant, flat type, and application status.
	 * The header row will be added at the beginning of the file.
	 */
	@Override
	public void saveApplicationtoCSV() {
		List<String> fileFormat = applicationList.values().stream()
				.flatMap(Set::stream)
				.map(data -> data.fileFormat())
				.collect(Collectors.toCollection(ArrayList::new));
		
		
		fileFormat.add(0, "Project Name,Applicant,FlatType,ApplicationStatus,PreviousStatus");
		fileHandler.writeToFile(APPLICATIONS_FILE,fileFormat);
	}
	
	/**
	 * Helper function. Creates an Application object from a CSV row.
	 *
	 * @param values the array of values from a CSV row (projectName, nric,
	 *               flatType, status)
	 * @return the created Application object, or null if the data is invalid
	 */
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
	
	/**
	 * Creates a new application for a user and project.
	 *
	 * @param user     the user applying for the project (must be an Applicant)
	 * @param project  the project to apply for
	 * @param flatType the flat type for the application
	 * @throws IllegalArgumentException if the user is invalid, the project is
	 *                                  invalid, or the user is an HDBOfficer
	 *                                  managing the project
	 */
	@Override
	public void createApplication(User user, Project project, FlatType flatType) {

		if (user == null || user instanceof HDBManager) {
			throw new IllegalArgumentException("Invalid applicant.");
		}

		if (project == null || LocalDate.now().isAfter(project.getClosingDate()) || !project.getVisibility()) {
			throw new IllegalArgumentException("Invalid project.");
		}

		Applicant applicant = (Applicant) user;

		if (applicant instanceof HDBOfficer officer && officer.getProjects().contains(project)) {
			throw new IllegalArgumentException("Cannot apply for a project you are managing.");
		}
		
		boolean hasExistingApplication = false;

		// Iterate through all the sets of applications in applicationList
		for (Set<Application> applications : applicationList.values()) {
		    // Check if there are any applications for the applicant
		    if (applications.removeIf(app -> app.getApplicant().equals(applicant))) {
		        hasExistingApplication = true;  // If we removed any, set flag to true
		    }
		}

		// Check if the applicant had an existing application
		if (hasExistingApplication) {
		    System.out.println("Existing applications have been removed.");
		} 
		Application a = new Application(applicant, project, flatType, ApplicationStatus.PENDING, ApplicationStatus.PENDING);
		applicationList.computeIfAbsent(project.getProjectName(), k -> new HashSet<>()).add(a);
		applicant.setApplication(a);
	}

	/**
	 * Retrieves application for a specific user.
	 *
	 * @param user the user to retrieve applications for (must be an Applicant)
	 * @return an application by the user
	 */
	@Override
	public Application getApplicationsForUser(User user) {
	    return applicationList.values().stream()
	        .flatMap(Set::stream)
	        .filter(a -> a.getApplicant().getNric().equals(user.getNric()))
	        .findFirst()
	        .orElse(null);  // Return null if no application is found
	}

	/**
	 * Retrieves all applications for a specific project.
	 *
	 * @param project the project to retrieve applications for
	 * @return a list of applications for the project, or an empty list if the
	 *         project is null or has no applications
	 */
	@Override
	public List<Application> getApplicationsForProject(Project project) {
		return applicationList.values().stream()
				.flatMap(Set::stream).filter(a -> a.getProject().equals(project))
				.toList();
	}

	/**
	 * Approves an application for a user and project. Reserves one flatType in the project.
	 *
	 * @param user    the applicant whose application is to be approved
	 * @param project the project associated with the application
	 * @throws IllegalArgumentException if the NRIC or project is null
	 */
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
	
	/**
	 * Rejects an application for a user and project.
	 *
	 * @param user    the applicant whose application is to be approved
	 * @param project the project associated with the application
	 * @throws IllegalArgumentException if the NRIC or project is null
	 */
	@Override
	public void rejectApplication(Application application) {
		
		if (application == null) {
			throw new IllegalArgumentException("Application cannot be null.");
		}
		
		application.setStatus(ApplicationStatus.UNSUCCESSFUL);
	}
	/**
	 * Approves a withdrawal request for a user.
	 *
	 * @param user the applicant whose withdrawal is to be approved
	 * @throws IllegalArgumentException if the user is null
	 */
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
	
	/**
	 * Rejects withdrawal of an application for a user.
	 *
	 * @param user the user requesting the withdrawal (must be an Applicant)
	 */
	@Override
	public void rejectWithdrawal(Application application) {
		
		if (application == null) {
			throw new IllegalArgumentException("application cannot be null.");
		}
		
		application.revertStatus();
		
	}

	/**
	 * Requests withdrawal of an application for a user.
	 *
	 * @param user the user requesting the withdrawal (must be an Applicant)
	 */
	@Override
	public void requestWithdrawal(Application application) {
		
		if (application == null) {
			throw new IllegalArgumentException("application cannot be null.");
		}
		application.setStatus(ApplicationStatus.WITHDRAW_REQUEST);
		
	}

	@Override
	public void bookFlat(Application application) {
		if (application == null) {
			throw new IllegalArgumentException("application cannot be null.");
		}
		application.setStatus(ApplicationStatus.BOOKED);
	}

	@Override
	public void writeReceipt(Application application) {
	    if (application == null) throw new IllegalArgumentException("Application cannot be null.");

	    String fileName = "receipt/receipt_" + application.getApplicant().getNric() + ".txt";
	    StringBuilder receipt = new StringBuilder();

	    receipt.append("==== HDB BTO Flat Booking Receipt ====\n");
	    receipt.append("Name: ").append(application.getApplicant().getName()).append("\n");
	    receipt.append("NRIC: ").append(application.getApplicant().getNric()).append("\n");
	    receipt.append("Age: ").append(application.getApplicant().getAge()).append("\n");
	    receipt.append("Marital Status: ").append(application.getApplicant().getMaritalStatus()).append("\n\n");

	    receipt.append("Flat Type Booked: ").append(application.getFlatType()).append("\n");
	    receipt.append("Project Name: ").append(application.getProject().getProjectName()).append("\n");
	    receipt.append("Location: ").append(application.getProject().getNeighbourhood()).append("\n");
	    receipt.append("Booking Status: ").append(application.getStatus()).append("\n");

	    fileHandler.writeToFile(fileName, List.of(receipt.toString()));
	}
	
	@Override
	public void viewReceipt(Application application) {
		if (application == null) throw new IllegalArgumentException("Application cannot be null.");
		
		String fileName = "receipt/receipt_" + application.getApplicant().getNric() + ".txt";
		fileHandler.readFromFile(fileName).stream().forEach(System.out::println);
	}
}
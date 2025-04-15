package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import enums.ApplicationStatus;
import enums.FlatType;
import enums.MaritalStatus;
import interfaces.IApplicationManager;
import interfaces.IFileHandler;
import interfaces.IProjectManager;
import interfaces.IUserManager;
import main.FileHandler;
import project.Project;
import project.ProjectManager;
import project.FlatTypeConverter;
import user.Applicant;
import user.HDBManager;
import user.HDBOfficer;
import user.User;
import user.UserManager;

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

	private static final String APPLICATIONS_FILE = "data/Applications.csv";

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
		fileHandler.readFromFile(APPLICATIONS_FILE).stream().skip(1).map(entry -> entry.split(","))
				.map(this::createApplicationFromFile).filter(application -> application != null)
				.forEach(application -> applicationList
						.computeIfAbsent(application.getProject().getProjectName(), k -> new HashSet<>())
						.add(application));
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
		FlatType flatType = FlatTypeConverter.convertToFlatType(values[2]);
		ApplicationStatus appStatus = ApplicationStatus.valueOf(values[3].toUpperCase());

		Project project = projectManager.getProject(projectName);
		Applicant user = (Applicant) userManager.getUser(nric);

		return new Application(user, project, flatType, appStatus);
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

		if (project == null || LocalDate.now().isAfter(project.getClosingDate()) || project.getVisibility() == false) {
			throw new IllegalArgumentException("Invalid project.");
		}

		Applicant applicant = (Applicant) user;

		if (applicant instanceof HDBOfficer officer && officer.getProjects().contains(project)) {
			throw new IllegalArgumentException("Cannot apply for a project you are managing.");
		}

		Application a = new Application(applicant, project, flatType, ApplicationStatus.PENDING);
		applicationList.computeIfAbsent(project.getProjectName(), k -> new HashSet<>()).add(a);
		applicant.applyForProject(a);
	}

	@Override
	public Map<String, Set<Application>> getApplicationList() {
		return this.applicationList;
	}

	/**
	 * Retrieves application for a specific user.
	 *
	 * @param user the user to retrieve applications for (must be an Applicant)
	 * @return an application by the user
	 */
	@Override
	public Application getApplicationsForUser(User user) {
		return applicationList.values().stream().flatMap(Set::stream)
				.filter(a -> a.getApplicant().getNric().equals(user.getNric())).toList().getFirst();
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
		return applicationList.values().stream().flatMap(Set::stream).filter(a -> a.getProject().equals(project))
				.toList();
	}

	/**
	 * Approves an application for a user and project.
	 *
	 * @param user    the applicant whose application is to be approved
	 * @param project the project associated with the application
	 * @throws IllegalArgumentException if the NRIC or project is null
	 */
	@Override
	public void approveApplication(User user) {

		// Call this and ask for input etc.
		/* getApplicationsForProject(project).forEach(System.out::println); */

		applicationList.values().stream().flatMap(Set::stream)
				.filter(a -> a.getApplicant().getNric().equals(user.getNric())).findFirst()
				.ifPresent(a -> a.setStatus(ApplicationStatus.SUCCESSFUL));
	}

	/**
	 * Approves a withdrawal request for a user.
	 *
	 * @param user the applicant whose withdrawal is to be approved
	 * @throws IllegalArgumentException if the user is null
	 */
	@Override
	public void approveWithdrawal(User user) {

		// Call this and ask for input etc.
		/*
		 * getApplicationsForProject(project).stream() .filter(a ->
		 * a.getStatus().equals(ApplicationStatus.WITHDRAW_REQUEST))
		 * .forEach(System.out::println);
		 */

		if (user == null) {
			throw new IllegalArgumentException("User must not be null.");
		}

		for (Set<Application> applications : applicationList.values()) {
			applications.removeIf(a -> a.getApplicant().getNric().equals(user.getNric()));
		}

		if (user instanceof Applicant applicant) {
			applicant.removeApplication();
		}
	}

	/**
	 * Requests withdrawal of an application for a user.
	 *
	 * @param user the user requesting the withdrawal (must be an Applicant)
	 * @return true if the withdrawal request was successful, false otherwise
	 */
	@Override
	public boolean requestWithdrawal(User user) {
		if (user instanceof Applicant applicant) {
			applicant.withdrawApplication(this);
			return true;
		}
		return false;
	}

}
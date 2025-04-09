package application;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import enums.ApplicationStatus;
import project.Project;
import user.Applicant;
import user.User;
import user.UserManager;

public class ApplicationManager {
	private List<Application> applicationList;
	
	public ApplicationManager() {
		this.applicationList = new ArrayList<>();
	}
	
	public void createApplication
	(UserManager userManager, ProjectManager projectManager, Scanner sc) {
		
		System.out.print("Enter NRIC: ");
		String nric = sc.next().toUpperCase();
		User user = userManager.getUser(nric);
		
		if (user == null || user instanceof HDBManager) {
			System.out.print("Invalid Applicant.");
			return;
		}
		Applicant applicant = (Applicant) user;
		
		System.out.print("Enter Project Name: ");
		String projectName = sc.nextLine().toLowerCase();
		Project project = projectManager.getProject(projectName);
		
		if (project == null 
				|| LocalDate.now().isAfter(project.getClosingDate())
				|| project.getVisibility() == false) {
			System.out.print("Invalid Project.");
			return;
		}
		
		Application a = new Application(applicant, project, null, ApplicationStatus.PENDING);
		applicationList.add(a);
		applicant.applyForProject(a);
	}
	
	public void printAll() {
		for (Application entry : applicationList) {
			System.out.println(entry);
		}
	}
	
	public void retrieveNRIC() {
		
	}
	
	public List<Application> viewApplication(){
		return this.applicationList;
	}
	
	public List<Application> getApplication(Project project){
		return applicationList.stream()
				.filter(entry -> 
						entry.getProject().getProjectName()
							.equals(project.getProjectName()))
				.toList();
	}
	
	public void approveApplication(Applicant applicant) {
		applicationList.stream()
			.filter(entry -> entry.getApplicant().getNric().equals(applicant.getNric()))
			.findFirst()
			.ifPresent(entry -> entry.setStatus(ApplicationStatus.SUCCESSFUL));
	}
	
	public void approveWithdrawal(Applicant applicant) {
		applicationList.removeIf(entry -> entry.getApplicant().getNric().equals(applicant.getNric()));
		applicant.removeApplication();
	}
	
	
	
	
}

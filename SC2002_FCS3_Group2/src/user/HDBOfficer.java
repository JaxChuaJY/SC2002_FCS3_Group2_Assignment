package user;

import java.util.List;

import enums.MaritalStatus;
import project.Project;

public class HDBOfficer extends Applicant {

	private List<Project> managedProject;
	
	public HDBOfficer(String name, String nric, int age, MaritalStatus maritalstatus, String password) {
		super(name, nric, age, maritalstatus, password);
		
	}
	
	
	public List<Project> getProjects() {
		return this.managedProject;
	}
	
	public void addProject(Project project) {
		boolean hasOverlap = managedProject.stream()
		        .anyMatch(entry ->
		            !(entry.getClosingDate().isBefore(project.getOpeningDate())
		            || entry.getOpeningDate().isAfter(project.getClosingDate()))
		        );
		if (hasOverlap) {
			System.out.println("Overlapping Projects.");
			return;
		}
		managedProject.add(project);
	}
}

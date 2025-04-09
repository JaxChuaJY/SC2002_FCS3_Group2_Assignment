package user;
import java.util.List;

import application.Application;
import enums.MaritalStatus;
import interfaces.IProjectViewer;
import project.Project;

public class Applicant extends User implements IProjectViewer{
	
	private Application application;
	
	public Applicant(String name, String nric, int age, 
			MaritalStatus maritalstatus, String password){
		super(name, nric, age, maritalstatus, password);
	}
	
	@Override
	public List<Project> viewProject(List<Project> projectList) {
		return projectList.stream()
				.filter(project -> 
						getMaritalStatus().canView(project.getFlatType(), getAge()))
		        .toList();
	}

	
}

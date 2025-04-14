package project;

import java.util.List;
import java.util.stream.Collectors;

import interfaces.IProjectManager;
import user.HDBOfficer;
import user.User;

public class ProjectManager implements IProjectManager {

	private List<Project> list;
	
	

	public ProjectManager(List<Project> input) {
		list = input;
	}

	public List<Project> getFilteredList(User user) {
		// TODO Auto-generated method stub
		return list.stream().filter(p -> projectREGviewCriteria((HDBOfficer) user, p)).collect(Collectors.toList());
	}

	public Project getProject(String projectName) {
		// TODO Auto-generated method stub
		List<Project> list = this.list.stream()
				.filter(p -> p.getProjectName().equals(projectName))
				.collect(Collectors.toList());
		
		if (list.size() == 1) {
		    return list.get(0);
		} else {
		    throw new IllegalStateException("Expected exactly one match, but found " + list.size());
		}
	}

	public boolean projectREGviewCriteria(HDBOfficer user, Project project) {

		if (user.getManagedProject().contains(project)) {
			return false;
		}

		if (user.getApplication() != null) {
			if (user.getApplication().getProject().equals(project)) {
				return false;
			}
		}

		for (Project userProject : user.getManagedProject()) {
			if (project.getOpeningDate().isBefore(userProject.getClosingDate())
					|| project.getOpeningDate().isEqual(userProject.getClosingDate())
							&& userProject.getOpeningDate().isBefore(project.getClosingDate())
					|| userProject.getOpeningDate().equals(project.getOpeningDate())) {
				return false;
			}
		}

		return true;
	}

	// REMEMBER TO REMOVE!
	public void printList() {

		System.out.println("============ProjectManager PRINT ALL============");

		for (Project p : list) {
			System.out.println("-----------");
			System.out.print(p);
			p.printOfficerList();
			p.printFlatSupply();
			System.out.println("----------");
		}
	}

}
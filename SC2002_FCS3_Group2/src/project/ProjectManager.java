package project;

import interfaces.IProjectManager;
import user.User;
import user.FilterSettings;



public class ProjectManager implements IProjectManager{

	public void viewProject(User user) {
		// TODO Auto-generated method stub
		
	}

	public Project getProject(String projectName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void filterView(FilterSettings filters) {
	    System.out.println("\n=== Filtered Projects ===");
	    for (Project p : projectList) {                 //I'm assuming the project will be store in some form of list 
	        // Filter by location
	        if (filters.getLocation() != null &&
	            !p.getNeighbourhood().equalsIgnoreCase(filters.getLocation())) {
	            continue;
	        }

	        // Filter by flat type
	        if (filters.getFlatType() != null &&
	            !p.getFlatTypes().contains(filters.getFlatType())) {
	            continue;
	        }

	        // Passed all filters
	        System.out.println("- " + p.getProjectName() + " @ " + p.getNeighbourhood());
	    }
	}


}

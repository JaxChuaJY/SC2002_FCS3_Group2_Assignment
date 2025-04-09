package main;

import application.ApplicationManager;
import user.User;
import user.UserManager;

public class BTOManagementSystem {
	//private List<Project> projectList;
	private UserManager userManager;
	private ApplicationManager applicationManager;
	
	BTOManagementSystem(){
		this.userManager = new UserManager();
	}
	
	public void startSystem() {
		this.userManager.initializeFromCSV();
		
		User user = userManager.loginUser();
        if (user != null) {
        	//
        } else {
            System.out.println("Login failed. Exiting system.");
        }
	}
	
}

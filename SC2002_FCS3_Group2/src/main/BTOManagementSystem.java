package main;

import java.util.Scanner;

import application.ApplicationManager;
import interfaces.IApplicationManager;
import interfaces.IProjectManager;
import interfaces.IUserManager;
import project.ProjectManager;
import user.User;
import user.UserManager;

public class BTOManagementSystem {
	private final IUserManager userManager;
    private final IProjectManager projectManager;
    private final IApplicationManager applicationManager;
	private Scanner sc;
	
	BTOManagementSystem(){
		userManager = new UserManager();
		projectManager = new ProjectManager();
		applicationManager = new ApplicationManager(projectManager,userManager);
	}
	
	public void startSystem() {
		
		User user = userManager.loginUser();
        if (user != null) {
        } else {
            System.out.println("Login failed. Exiting system.");
        }
	}
	
}

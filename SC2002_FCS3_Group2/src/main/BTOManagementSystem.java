package main;

import java.util.Scanner;

import application.ApplicationManager;
import interfaces.IApplicationManager;
import interfaces.IFileHandler;
import interfaces.IProjectManager;
import interfaces.IUserManager;
import project.ProjectManager;
import user.User;
import user.UserManager;

public class BTOManagementSystem {
	private final IUserManager userManager;
    private final IProjectManager projectManager;
    //private final IApplicationManager applicationManager;
	private final IFileHandler fileHandler;
	
	BTOManagementSystem(){
		fileHandler = new FileHandler();
		userManager = new UserManager(fileHandler);
		projectManager = new ProjectManager();
		//applicationManager = new ApplicationManager(projectManager,userManager,fileHandler);
	}
	
	public void startSystem() {
		
		User user = userManager.loginUser("S1234567A","password");
        if (user != null) {
        	System.out.print(user);
        } else {
            System.out.println("Login failed. Exiting system.");
        }
	}
	
}

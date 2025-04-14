package main;


import java.util.List;

import enums.ApplicationStatus;
import file.FileManager;
import interfaces.IProjectManager;
import interfaces.IUserManager;
import project.Project;
import project.ProjectManager;
import registration.ProjectRegistration;
import registration.RegistrationForm;
import user.HDBOfficer;
import user.UserManager;

public class BTOManagementSystem {
	private IUserManager userManager;
    private IProjectManager projectManager;
    private ProjectRegistration projectRegManager; //interface
    //private final IApplicationManager applicationManager;
	//private final IFileHandler fileHandler;
	
	BTOManagementSystem(){
		//fileHandler = new FileHandler();
		userManager = new UserManager(FileManager.readingALLUsers());
		projectManager = new ProjectManager(FileManager.readProject(userManager));
		projectRegManager = new ProjectRegistration(FileManager.readRegForms(userManager, projectManager));
		//applicationManager = new ApplicationManager(projectManager,userManager,fileHandler);
	}
	
	public void startSystem() {
		userManager.printAllUser();
		login();
        if (userManager.getcurrentUser() != null) {
        	System.out.println(userManager.getcurrentUser());
        } else {
            System.out.println("Login failed. Exiting system.");
        }
	}
	
	private void login() {
		do {
			userManager.login();
		} while (userManager.getcurrentUser() == null);
	}
	
	public void changePassword() { 
		FileManager.writeFile_changePW(userManager.changePassword(), userManager.getcurrentUser());
		userManager.logout();
		
		try {
			userManager.setList(FileManager.readingALLUsers());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		login();
		
	}
	
	public void showMenu() {
		userManager.getcurrentUser().showMenu(this);
	}
	
	public void newRegisterProject(Project p) {
		projectRegManager.register(p, (HDBOfficer)userManager.getcurrentUser());
		FileManager.writeFile_newRegForm(p, (HDBOfficer)userManager.getcurrentUser());
	}
	
	public List<Project> filterListRegister(){
		List<Project> filteredList = projectManager.getFilteredList(userManager.getcurrentUser());
		return projectRegManager.getNonRegList(filteredList, (HDBOfficer) userManager.getcurrentUser());
	}
	
	public void setRegistrationStatus(RegistrationForm f) {
		if (f.getStatus() == ApplicationStatus.APPROVED) {
			//Write the officer List inside
			f.getProject().addOfficer(f.getRegisteredBy());
			f.getRegisteredBy().addProject(f.getProject());
			FileManager.writeFile_addOfficer(f);
		}
		
		
		FileManager.writeFile_setRegForm(f);
	}
	
	/**
	 * @return the userManager
	 */
	public IUserManager getUserManager() {
		return userManager;
	}

	/**
	 * @return the projectManager
	 */
	public IProjectManager getProjectManager() {
		return projectManager;
	}

	/**
	 * @return the projectRegManager
	 */
	public ProjectRegistration getProjectRegManager() {
		return projectRegManager;
	}
	
}
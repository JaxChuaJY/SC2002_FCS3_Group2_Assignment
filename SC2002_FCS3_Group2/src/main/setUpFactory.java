package main;

import application.ApplicationManager;
import enquiry.EnquiryManager;
import project.ProjectManager;
import registration.ProjectRegistration;
import user.UserManager;
import interfaces.IApplicationManager;
import interfaces.IFileHandler;
import interfaces.IProjectManager;
import interfaces.IUserManager;
import interfaces.IProjectRegistration;

public class setUpFactory {
	public static BTOManagementSystem createSystem() {
		IFileHandler fileHandler = new FileHandler();
        IUserManager userManager = new UserManager(fileHandler);
        IProjectManager projectManager = new ProjectManager(fileHandler, userManager);
        IProjectRegistration projectRegManager = new ProjectRegistration(userManager, projectManager);
        IApplicationManager applicationManager = new ApplicationManager(projectManager, userManager, fileHandler);
        EnquiryManager enquiryManager = new EnquiryManager();

        return new BTOManagementSystem(fileHandler, 
                                     userManager, 
                                     projectManager, 
                                     projectRegManager, 
                                     applicationManager, 
                                     enquiryManager);
	}
}

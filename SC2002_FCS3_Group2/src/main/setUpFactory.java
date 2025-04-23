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


/**
 * Factory class to create and configure a fully initialized instance of the BTOManagementSystem.
 * <p>
 * This class sets up all necessary dependencies and injects them into the system.
 */
public class setUpFactory {
	/**
     * Creates and returns a fully configured instance of the BTOManagementSystem.
     * <p>
     * This includes instantiating all managers and file handlers required by the system,
     * such as {@code FileHandler}, {@code UserManager}, {@code ProjectManager},
     * {@code ProjectRegistration}, {@code ApplicationManager}, and {@code EnquiryManager}.
     * All dependencies are injected through constructor-based dependency injection.
     *
     * @return A fully initialized {@link BTOManagementSystem} instance with all managers set up.
     * @throws Exception if any error occurs during initialization.
     */
	public static BTOManagementSystem createSystem() throws Exception {
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

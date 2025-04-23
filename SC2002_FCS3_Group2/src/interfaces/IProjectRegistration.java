package interfaces;

import project.Project;
import registration.RegistrationForm;
import user.HDBOfficer;

/**
 * Handles HDB Officer registration workflows for projects.
 */
public interface IProjectRegistration {

    /**
     * Registers an HDB Officer to a specified project.
     *
     * @param project the project to register for
     * @param user the HDB Officer performing registration
     */
	void register(Project project, HDBOfficer user);
	
	/**
     * Approves a pending registration form.
     *
     * @param form the RegistrationForm to approve
     */
	void approveRegistration(RegistrationForm form);
	
	/**
     * Rejects a pending registration form.
     *
     * @param form the RegistrationForm to reject
     */
	void rejectRegistration(RegistrationForm form);
	
}

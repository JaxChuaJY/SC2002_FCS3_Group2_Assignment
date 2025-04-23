package interfaces;

import java.util.List;

import project.Project;
import registration.RegistrationForm;
import user.HDBManager;
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
	void writeFile_setRegForm(RegistrationForm selected);
	void writeFile_addOfficer(RegistrationForm selected);
	void printList_ManagerFilter(HDBManager user);
	List<RegistrationForm> getFilteredList(HDBManager user);
	void printList_officer(HDBOfficer user);
	List<Project> getNonRegList(List<Project> filteredList, HDBOfficer user);
	
}

package interfaces;

import java.util.List;

import project.Project;
import registration.RegistrationForm;
import user.HDBManager;
import user.HDBOfficer;

public interface IProjectRegistration {

	void register(Project project, HDBOfficer user);
	void approveRegistration(RegistrationForm form);
	void rejectRegistration(RegistrationForm form);
	void writeFile_setRegForm(RegistrationForm selected);
	void writeFile_addOfficer(RegistrationForm selected);
	void printList_ManagerFilter(HDBManager user);
	List<RegistrationForm> getFilteredList(HDBManager user);
	void printList_officer(HDBOfficer user);
	List<Project> getNonRegList(List<Project> filteredList, HDBOfficer user);
	
}

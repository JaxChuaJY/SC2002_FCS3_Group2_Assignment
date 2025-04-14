package interfaces;

import project.Project;
import registration.RegistrationForm;
import user.HDBOfficer;

public interface IProjectRegistration {

	void register(Project project, HDBOfficer user);
	void approveRegistration(RegistrationForm form);
	void rejectRegistration(RegistrationForm form);
	
}

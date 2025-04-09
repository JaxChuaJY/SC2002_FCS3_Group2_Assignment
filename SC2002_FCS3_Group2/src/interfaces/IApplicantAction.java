package interfaces;

import application.Application;

public interface IApplicantAction {
	void withdrawApplication();
	String viewApplicationStatus();
	void applyForProject(Application a);
	void removeApplication();
}

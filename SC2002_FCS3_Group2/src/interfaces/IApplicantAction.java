package interfaces;

import application.Application;
import application.ApplicationManager;

public interface IApplicantAction {
	String viewApplicationStatus();
	void applyForProject(Application a);
	void removeApplication();
	void withdrawApplication(ApplicationManager aM);
}
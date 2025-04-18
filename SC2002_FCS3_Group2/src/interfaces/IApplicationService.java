package interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import application.Application;
import enums.FlatType;
import project.Project;
import user.Applicant;

public interface IApplicationService {

	void createApplication(Applicant applicant, Project project, FlatType flatType);

	Application getApplicationForApplicant(Applicant applicant);

	List<Application> getApplicationsForProject(Project project);

	void approveApplication(Application application);

	void rejectApplication(Application application);

	void requestWithdrawal(Application application);

	void approveWithdrawal(Application application);

	void rejectWithdrawal(Application application);

	void bookFlat(Application application);

	void addApplication(Application application);

	Map<String, Set<Application>> getApplicationList();

}

package interfaces;

import java.util.List;

import enums.FlatType;
import project.Project;
import user.FilterSettings;
import user.HDBManager;
import user.User;

public interface IProjectManager {

	Project getProject(String projectName);

	List<Project> getFilteredList_Register(User user);

	List<Project> getProjectList(User user);
	
	void viewAllProj(User user);

	boolean updateFlatRooms(Project project, FlatType flatType, int x);

	void addProject(HDBManager manager);

	boolean removeProject(String nextLine);

	void filterView(FilterSettings filters, HDBManager user);
	
}
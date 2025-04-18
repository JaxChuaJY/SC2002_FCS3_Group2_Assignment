package interfaces;

import java.util.List;

import enums.FlatType;
import project.Project;
import user.User;

public interface IProjectManager {

	Project getProject(String projectName);

	List<Project> getFilteredList_Register(User user);

	List<Project> getProjectList(User user);

	boolean updateFlatRooms(Project project, FlatType flatType, int x);
	
}
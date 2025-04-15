package interfaces;

import java.util.List;

import project.Project;
import user.User;

public interface IProjectManager {

	Project getProject(String projectName);

	List<Project> getFilteredList_Register(User user);
	
	
}
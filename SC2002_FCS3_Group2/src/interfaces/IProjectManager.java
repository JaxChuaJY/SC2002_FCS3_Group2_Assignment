package interfaces;

import java.util.List;

import enums.FlatType;
import project.Project;
import user.FilterSettings;
import user.HDBManager;
import user.User;

/**
 * Manages BTO project operations including retrieval, filtering,
 * and modifications by HDB Managers and viewing by users.
 */
public interface IProjectManager {

	/**
     * Retrieves a project by its name.
     *
     * @param projectName the name of the project
     * @return the matching Project object, or null if not found
     */
	Project getProject(String projectName);

	/**
     * Retrieves a list of projects filtered based on user settings.
     *
     * @param user the user whose filter settings apply
     * @return list of Project objects matching the filters
     */
	List<Project> getFilteredList_Register(User user);

	/**
     * Retrieves the full project list accessible to the user.
     *
     * @param user the user requesting the project list
     * @return list of Project objects viewable by the user
     */
	List<Project> getProjectList(User user);
	
	/**
     * Displays all projects that the user has access to view.
     *
     * @param user the user requesting the list
     */
	void viewAllProj(User user);
 
 	/**
     * Updates the number of available flats of a given type in a project.
     *
     * @param project the project to update
     * @param flatType the type of flat to adjust
     * @param x the new count of available units
     * @return true if the update was successful, false otherwise
     */
	boolean updateFlatRooms(Project project, FlatType flatType, int x);

	/**
     * Adds a new project to the system under a specific manager.
     *
     * @param manager the HDB Manager creating the project
     */
	void addProject(HDBManager manager);

	/**
     * Removes a project, given its data line or identifier.
     *
     * @param nextLine the CSV line or identifier representing the project
     * @return true if removal succeeded, false otherwise
     */
	boolean removeProject(String nextLine);

	/**
     * Applies filter settings to the project list for a user.
     *
     * @param filters the FilterSettings criteria
     * @param user the HDB Manager applying the filter
     */
	void filterView(FilterSettings filters, HDBManager user);

	/**
     * Edits the details of an existing project.
     *
     * @param proj the Project object with updated information
     */
	void editProject(Project proj);

	/**
     * Edits the details of an existing project.
     *
     * @param proj the Project object with updated information
     */
	void toggleVisibility(Project proj);
	
}
package user;

import enums.FlatType;

/**
 * Represents user-defined filters for viewing BTO projects.
 * <p>
 * Users can specify a location, flat type, and whether
 * managers see all projects or only those they manage.
 * </p>
 */
public class FilterSettings {
    
    /** Desired neighbourhood to filter projects by. */
    private String location;
    
    /** Desired flat type to filter projects by. */
    private FlatType flatType;
    
    /** Flag indicating if managers view all projects (true) or only their own (false). */
    private boolean managerViewALL; //see all? Y or N

    /**
     * Constructs default filter settings with no location or flat type,
     * and managers viewing all projects.
     */
    public FilterSettings() {
        this.location = null;
        this.flatType = null;
        managerViewALL = true;
    }

    /**
     * Sets the location filter.
     *
     * @param location the neighbourhood name to filter by
     */
    public void setLocation(String location) {
    	if (location.equals("None")) {
     		this.location = null;
     	}else {
     		this.location = location;
     	}
    }

    /**
     * Sets the flat type filter.
     *
     * @param flatType the {@link FlatType} to filter by
     */
    public void setFlatType(FlatType flatType) {
        this.flatType = flatType;
    }

    /**
     * Retrieves the current location filter.
     *
     * @return the neighbourhood name, or null if none set
     */
    public String getLocation() {
        return location;
    }

    /**
     * Retrieves the current flat type filter.
     *
     * @return the {@link FlatType}, or null if none set
     */
    public FlatType getFlatType() {
        return flatType;
    }
    
    /**
     * Sets whether a manager views all projects or only their own.
     *
     * @param b true if viewing all projects, false to restrict to own projects
     */   
    public void setmanagerViewALL(boolean b) {
    	managerViewALL = b;
    }

    /**
     * Resets all filters to their default (no location or flat type).
     */
    public void reset() {
        this.location = null;
        this.flatType = null;
    }

    /**
     * Checks if managers are set to view all projects.
     *
     * @return true if managers view all projects, false otherwise
     */
    public boolean getmanagerViewALL(){
    	return managerViewALL;
    }
}
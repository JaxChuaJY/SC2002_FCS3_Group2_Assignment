package project;

import enums.FlatType;
import user.HDBManager;
import user.HDBOfficer;

import java.time.LocalDate;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;	


/**
 * Represents a Build-To-Order (BTO) project, including its name,
 * neighbourhood, available flat supply, application date window,
 * visibility status, and assigned HDB Officers.
 */
public class Project {
	
	/** Name of the BTO project. */
	protected String projectName;
	
	/** Neighbourhood where the project is located. */
	protected String neighbourhood;
	
	/** Map of flat types to their available count and cost. */
	protected EnumMap<FlatType, SimpleEntry<Integer, Double>> flatSupply;
	
	/** Application opening date. */
	protected LocalDate openingDate;
	
	/** Application closing date. */
	protected LocalDate closingDate;
	
	/** HDB Manager handling this project. */
	protected HDBManager manager;
	
	/** Number of possible HDB Officers handling this project. */
	protected int officerSlots;
	
	/** List of HDB Officers handling this project. */
	protected List<HDBOfficer> officerList;
	
	/** Whether the project is visible to applicants. */
	private boolean isVisible;
	
	/**
     * Default constructor.
     */
	public Project() { 
	}

	/**
     * Parameterized constructor to initialize project details.
     *
     * @param name       projectName
     * @param area       neighbourhood
     * @param flatMap    flat supply mapping
     * @param date1      openingDate
     * @param date2      closingDate
     * @param offSlots   number of officer slots
     */
	public Project(String name, String area, EnumMap<FlatType, SimpleEntry<Integer, Double>> flatMap, LocalDate date1, LocalDate date2, int offSlots) {
		this.projectName = name;
		this.neighbourhood = area;
		this.flatSupply = flatMap;
		this.openingDate = date1;
		this.closingDate = date2;
		this.officerSlots = offSlots;
		this.officerList = new ArrayList<HDBOfficer>();
		if ((openingDate.isBefore(LocalDate.now()) || openingDate.isEqual(LocalDate.now())) &&
 			    (closingDate.isAfter(LocalDate.now()) || closingDate.isEqual(LocalDate.now()))){
 			this.isVisible = true;
 		}else {
 			this.isVisible = false;
 		}
	}
	
	
	//-------Misc.
	/**
     * Toggles project visibility on or off.
     */
	public void toggleVisibility() {
		isVisible = !isVisible;
	}
	
	/**
     * Checks if officer slots are still available.
     *
     * @return true if additional officers can register; false otherwise
     */
	public boolean checkOfficerSlotCap() {
		if (officerList.size() >= officerSlots) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
     * Returns a summary string of project details.
     *
     * @return formatted project information
     */
	public String toString() {
		return "Project: " + projectName
			     + "\n\tNeighbourhood: " + neighbourhood
			     + "\n\tFlat Supply: " + getFlatTypes_String() 
			     + "\n\tManaged by: " + manager.getName();
	}

	//-------ADDING
	/**
     * Assigns the project manager.
     *
     * @param manager the HDBManager to assign
     */
	public void addManager(HDBManager manager) {
		this.manager = manager;
	}
	
	/**
     * Adds an HDB officer to this project.
     *
     * @param officer the HDBOfficer to add
     */
	public void addOfficer(HDBOfficer officer) {
		officerList.add(officer);
	}
	
	//-------PRINTING STATEMENTS
	/**
     * Prints flat supply details to console.
     */
	public void printFlatSupply() {
		System.out.print("Flat Types and available units:");
		if (flatSupply.isEmpty()) {
			System.out.print("No flats lmao");
		}
		else {
			for (Entry<FlatType, SimpleEntry<Integer, Double>> entry : flatSupply.entrySet()) {
				System.out.print(entry.getKey()+ ": " +entry.getValue()+ " units");
			}
		}
	}
	
	/**
     * Prints assigned officers to console.
     */
	public void printOfficerList() {
		System.out.println("\nOfficers: ");
		if (officerList.isEmpty()) {
			System.out.println("\tNO OFFICERS!");
		}else {
			for (HDBOfficer officer : officerList) {
				System.out.println("\t"+officer.getName());
				
			}
		}
	}
	
	
	//-------GETTER AND SETTERS
	/**
     * Retrieves the project name.
     *
     * @return projectName
     */
	public String getProjectName() {
		return projectName;
	}

	/**
     * Sets the project name.
     *
     * @param projectName the new project name
     */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
     * Retrieves the neighbourhood.
     *
     * @return neighbourhood
     */
	public String getNeighbourhood() {
		return neighbourhood;
	}

	/**
     * Sets the neighbourhood.
     *
     * @param neighbourhood the new neighbourhood
     */
	public void setNeighbourhood(String neighbourhood) {
		this.neighbourhood = neighbourhood;
	}
	
	/**
     * Gets the set of flat types available in this project.
     *
     * @return set of FlatType
     */
	public Set<FlatType> getFlatTypes() {         // For filtering
	    return flatSupply.keySet();
	}
	
	/**
     * Returns flat types and their unit counts as a string.
     *
     * @return flat supply summary string
     */
	public String getFlatTypes_String() {
		String s = "";
		for (Entry<FlatType, SimpleEntry<Integer, Double>> entry : flatSupply.entrySet()) {
			s += (entry.getKey()+ ": " +entry.getValue().getKey()+ " units ");
		}
		return s;
	}

	/**
     * Retrieves the flat supply map.
     *
     * @return flatSupply map
     */
	public EnumMap<FlatType, SimpleEntry<Integer, Double>> getFlatSupply() {
		return flatSupply;
	}

	/**
     * Sets the flat supply map.
     *
     * @param flatSupply new flat supply mapping
     */
	public void setFlatSupply(EnumMap<FlatType, SimpleEntry<Integer, Double>> flatSupply) {
		this.flatSupply = flatSupply;
	}

	/**
     * Retrieves the opening date.
     *
     * @return openingDate
     */
	public LocalDate getOpeningDate() {
		return openingDate;
	}

	/**
     * Sets the opening date.
     *
     * @param openingDate the new opening date
     */
	public void setOpeningDate(LocalDate openingDate) {
		this.openingDate = openingDate;
	}

	/**
     * Retrieves the closing date.
     *
     * @return closingDate
     */
	public LocalDate getClosingDate() {
		return closingDate;
	}

	/**
     * Sets the closing date.
     *
     * @param closingDate the new closing date
     */
	public void setClosingDate(LocalDate closingDate) {
		this.closingDate = closingDate;
	}

	/**
     * Retrieves the manager in charge.
     *
     * @return manager
     */
	public HDBManager getManager() {
		return manager;
	}

	/**
     * Sets the project manager.
     *
     * @param manager the HDBManager to assign
     */
	public void setManager(HDBManager manager) {
		this.manager = manager;
	}

	/**
     * Retrieves the number of officer slots.
     *
     * @return officerSlots
     */
	public int getOfficerSlots() {
		return officerSlots;
	}

	/**
     * Sets the number of officer slots.
     *
     * @param officerSlots the new officer slots count
     */
	public void setOfficerSlots(int officerSlots) {
		this.officerSlots = officerSlots;
	}

	/**
     * Retrieves the list of assigned officers.
     *
     * @return officerList
     */
	public List<HDBOfficer> getOfficerList() {
		return officerList;
	}

	/**
     * Sets the list of assigned officers.
     *
     * @param officerList new list of HDBOfficer
     */
	public void setOfficerList(List<HDBOfficer> officerList) {
		this.officerList = officerList;
	}

	/**
     * Sets the visibility flag for applicants.
     *
     * @param isVisible new visibility status
     */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	/**
     * Retrieves the visibility flag.
     *
     * @return isVisible
     */
	public boolean getVisibility() {
		// TODO Auto-generated method stub
		return isVisible;
	}


	 /**
     * Updates the supply count of a given flat type by a delta.
     *
     * @param flatType the flat type to update
     * @param x        the change in unit count (positive or negative)
     * @return true if update succeeded; false if flatType not present or insufficient units
     */
	public boolean updateFlatSupply(FlatType flatType, int x) {
 		SimpleEntry<Integer, Double> entry = flatSupply.get(flatType);
 		if (entry == null || entry.getKey() <= 0) {
	        return false;
	    }
 		flatSupply.put(flatType, new AbstractMap.SimpleEntry<>(entry.getKey() + x, entry.getValue()));
 		return true;
 	}
	
	
}

package project;

import enums.FlatType;
import enums.MaritalStatus;
import user.Applicant;
import user.HDBManager;
import user.HDBOfficer;

import java.time.LocalDate;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;	

public class Project {
	protected String projectName;
	protected String neighbourhood;
	protected EnumMap<FlatType, SimpleEntry<Integer, Double>> flatSupply;
	protected LocalDate openingDate;
	protected LocalDate closingDate;
	protected HDBManager manager;
	protected int officerSlots;
	protected List<HDBOfficer> officerList;
	private boolean isVisible;
	
	
	public Project() { 
	}

	public Project(String name, String area, EnumMap<FlatType, SimpleEntry<Integer, Double>> flatMap, LocalDate date1, LocalDate date2, int offSlots) {
		this.projectName = name;
		this.neighbourhood = area;
		this.flatSupply = flatMap;
		this.openingDate = date1;
		this.closingDate = date2;
		this.officerSlots = offSlots;
		this.officerList = new ArrayList<HDBOfficer>();
		if (openingDate.isBefore(LocalDate.now()) && closingDate.isBefore(LocalDate.now()) || openingDate.isEqual(LocalDate.now()) || closingDate.isEqual(LocalDate.now())) {
			this.isVisible = true;
		}
	}
	
	
	//-------Misc.
	public void toggleVisibility() {
		isVisible = !isVisible;
	}
	
	public boolean checkOfficerSlotCap() {
		if (officerList.size() >= officerSlots) {
			return false;
		}else {
			return true;
		}
	}
	
	
	public String toString() {
		return "Project: " + projectName
			     + "\n\tNeighbourhood: " + neighbourhood
			     + "\n\tFlat Supply: " + getFlatTypes_String() 
			     + "\n\tManaged by: " + manager.getName();
	}

	//-------ADDING
	
	public void addManager(HDBManager manager) {
		this.manager = manager;
	}
	
	public void addOfficer(HDBOfficer officer) {
		officerList.add(officer);
	}
	
	//-------PRINTING STATEMENTS
	
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
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getNeighbourhood() {
		return neighbourhood;
	}

	public void setNeighbourhood(String neighbourhood) {
		this.neighbourhood = neighbourhood;
	}
	
	public Set<FlatType> getFlatTypes() {         // For filtering
	    return flatSupply.keySet();
	}
	
	public String getFlatTypes_String() {
		String s = "";
		for (Entry<FlatType, SimpleEntry<Integer, Double>> entry : flatSupply.entrySet()) {
			s += (entry.getKey()+ ": " +entry.getValue().getKey()+ " units ");
		}
		return s;
	}

	public EnumMap<FlatType, SimpleEntry<Integer, Double>> getFlatSupply() {
		return flatSupply;
	}

	public void setFlatSupply(EnumMap<FlatType, SimpleEntry<Integer, Double>> flatSupply) {
		this.flatSupply = flatSupply;
	}

	public LocalDate getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(LocalDate openingDate) {
		this.openingDate = openingDate;
	}

	public LocalDate getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(LocalDate closingDate) {
		this.closingDate = closingDate;
	}

	public HDBManager getManager() {
		return manager;
	}

	public void setManager(HDBManager manager) {
		this.manager = manager;
	}

	public int getOfficerSlots() {
		return officerSlots;
	}

	public void setOfficerSlots(int officerSlots) {
		this.officerSlots = officerSlots;
	}

	public List<HDBOfficer> getOfficerList() {
		return officerList;
	}

	public void setOfficerList(List<HDBOfficer> officerList) {
		this.officerList = officerList;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public boolean getVisibility() {
		// TODO Auto-generated method stub
		return isVisible;
	}


	public boolean updateFlatSupply(FlatType flatType, int x) {
 		SimpleEntry<Integer, Double> entry = flatSupply.get(flatType);
 		if (entry == null || entry.getKey() <= 0) {
	        return false;
	    }
 		flatSupply.put(flatType, new AbstractMap.SimpleEntry<>(entry.getKey() + x, entry.getValue()));
 		return true;
 	}
	
	
}

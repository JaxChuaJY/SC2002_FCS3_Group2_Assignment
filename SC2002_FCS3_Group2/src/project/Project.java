package group_project;

import enums.FlatType;

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
	
	public Project(String name, String area, EnumMap<FlatType, SimpleEntry<Integer, Double>> flatMap, LocalDate date1, LocalDate date2, int offSlots) {
		this.projectName = name;
		this.neighbourhood = area;
		this.flatSupply = flatMap;
		this.openingDate = date1;
		this.closingDate = date2;
		this.officerSlots = offSlots;
	}
	public String getProjectName() {
		return projectName;
	}
	public String getNeighbourhood() {
		return neighbourhood;
	}
	public Set<FlatType> getFlatTypes() {         // For filtering
	    return flatSupply.keySet();
	}
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
	public EnumMap<FlatType, SimpleEntry<Integer, Double>> getFlatSupply(){
		return flatSupply;
	}
	public LocalDate getOpeningDate() {
		return openingDate;
	}
	public LocalDate getClosingDate() {
		return closingDate;
	}
	public int getOfficarSlots() {
		return officerSlots;
	}
	public String getManagerName() {
		return manager.getName();
	}
	public void printOfficerList() {
		for (HDBOfficer officer : officerList) {
			officer.getName();
		}
	}
	public void addManager(HDBManager manager) {
		this.manager = manager;
	}
	public void addOfficer(HBDOfficer officer) {
		officerList.add(officer);
		//officerslots -1 		Only if this means empty slots
	}
	public void toggleVisibility() {
		isVisible = !isVisible;
	}
	public String toString() {
		return "Project: " + projectName
			     + "\nNeighbourhood: " + neighbourhood
			     + "\nFlat Supply: " + flatSupply.toString();
	}
}

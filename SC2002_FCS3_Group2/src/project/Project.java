package project;

import enums.FlatType;

import java.time.LocalDate;
import java.util.*;	

public class Project {
	protected String projectName;
	protected String neighbourhood;
	protected Map<FlatType, Integer> flatSupply;
	protected LocalDate openingDate;
	protected LocalDate closingDate;
	protected HDBManager manager;
	protected int officerSlots;
	protected List<HDBOfficer> officerList;
	private boolean isVisible;

	public Project(String name, String area, LocalDate date1, LocalDate date2) {	//How tf are we gonna 1) ask for input and 2) input the flatype numbers???
		this.projectName = name;
		this.neighbourhood = area;
		this.openingDate = date1;
		this.closingDate = date2;
	}
	public String getProjectName() {
		return projectName;
	}
	public String getNeighbourhood() {
		return neighbourhood;
	}
	public void printFlatSupply() {									//Do we need a getFlatSupply()?
		System.out.print("Flat Types and available units:");
		if (flatSupply.isEmpty()) {
			System.out.print("No flats lmao");
		}
		else {
			for (Map.Entry<FlatType, Integer> entry : flatSupply.entrySet()) {
				System.out.print(entry.getKey()+ ": " +entry.getValue()+ " units");
			}
		}
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
	public void toggleVisibility() {
		isVisible = !isVisible;
	}
}

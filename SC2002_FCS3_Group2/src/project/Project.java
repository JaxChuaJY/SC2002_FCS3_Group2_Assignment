package project;

import enums.FlatType;
import user.HDBManager;
import user.HDBOfficer;

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
	protected List<HDBOfficer> officerList = new ArrayList<HDBOfficer> ();
	private boolean isVisible;
	
	
	public Project() { 
	}

	public Project(String name, String area, LocalDate date1, LocalDate date2) {	
		this.projectName = name;
		this.neighbourhood = area;
		this.openingDate = date1;
		this.closingDate = date2;
	}
	
	public void printFlatSupply() {									//Do we need a getFlatSupply()?
		System.out.println("Flat Types and available units:");
		if (flatSupply.isEmpty()) {
			System.out.print("No flats lmao");
		}
		else {
			for (Map.Entry<FlatType, Integer> entry : flatSupply.entrySet()) {
				System.out.println("\t"+entry.getKey()+ ": " +entry.getValue()+ " units");
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
	
	public void toggleVisibility() {
		isVisible = !isVisible;
	}
	
	public String toString() {
		return "Project: " + projectName
			     + "\n\tNeighbourhood: " + neighbourhood
			     + "\n\tFlat Supply: " + flatSupply.toString() //Fix flatSupply print.
			     + "\n\tManaged by: " + manager.getName();
	}

	public void addOfficer(HDBOfficer e) {
		officerList.add(e);
		
	}
	
	public boolean checkOfficerSlotCap() {
		if (officerList.size() >= officerSlots) {
			return false;
		}else {
			return true;
		}
	}
	
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

	public Map<FlatType, Integer> getFlatSupply() {
		return flatSupply;
	}

	public void setFlatSupply(Map<FlatType, Integer> flatSupply) {
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

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public boolean getVisibility() {
		// TODO Auto-generated method stub
		return isVisible;
	}
	
	
}
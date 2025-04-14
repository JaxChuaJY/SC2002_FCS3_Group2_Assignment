package group_project;

import interfaces.IProjectManager;
import user.User;
import user.FilterSettings;

import java.io.*;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class ProjectManager implements IProjectManager{
	private List<Project> projectList;
	public String filePath = "src/ProjectList.csv";
	
	public ProjectManager() {
		projectList = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			DateTimeFormatter format = DateTimeFormatter.ofPattern("M/d/yyyy");
			br.readLine();				//Skip Header
			
			while ((line = br.readLine())!=null) {
				String[] items = line.split(","); 
				EnumMap<FlatType, SimpleEntry<Integer, Double>> flatMap = new EnumMap<>(FlatType.class);
				//Mapping of FlatType with number of units + price
				if (items[2].equalsIgnoreCase("two-room")) {
					flatMap.put(FlatType.TWO_ROOM, new SimpleEntry(Integer.parseInt(items[3]), Double.parseDouble(items[4])));
				}
				else {
					flatMap.put(FlatType.THREE_ROOM, new SimpleEntry(Integer.parseInt(items[3]), Double.parseDouble(items[4])));
				}
				if (items[5].equalsIgnoreCase("two-room")) {
					flatMap.put(FlatType.TWO_ROOM, new SimpleEntry(Integer.parseInt(items[6]), Double.parseDouble(items[7])));
				}
				else {
					flatMap.put(FlatType.THREE_ROOM, new SimpleEntry(Integer.parseInt(items[6]), Double.parseDouble(items[7])));
				}
				
				String name = items[0];
				String neighbourhood = items[1];
				LocalDate openDate = LocalDate.parse(items[8], format);
				LocalDate closeDate = LocalDate.parse(items[9], format);
				int offSlots = Integer.parseInt(items[11]);					//Officer Slots mean empty slots left or total slots?
				Project project = new Project(name, neighbourhood, flatMap, openDate, closeDate, offSlots);
				if (!items[10].isEmpty()) {
					//getManager from manager list using name
					project.addManager(HDBManager);
				}
				if (!items[12].isEmpty()) {
					String[] offList = items[12].split(",");
					for (String offname : offList) {
					//getOfficer from officer manager using name
					project.addOfficer(HDBOfficer);
					}
				}
				projectList.add(project);
			}
			
		}catch (IOException e) {
            e.printStackTrace();
        }
		
	}	
	public void addProject(User manager) {
		Scanner sc = new Scanner(System.in);
		EnumMap<FlatType, SimpleEntry<Integer, Double>> flatMap = new EnumMap<>(FlatType.class);
		
		System.out.print("\nEnter name of the Project: ");
		String name = sc.nextLine();
		System.out.print("\nEnter Neighbourhood: ");
		String neighbourhood = sc.nextLine();
		System.out.print("\nEnter Number of 2-room flats: ");
		int twoRoom_num = sc.nextInt();
		if (twoRoom_num !=0) {
			System.out.print("\nEnter Price of a 2-room flat: ");
			Double twoRoom_price = sc.nextDouble();
			flatMap.put(FlatType.TWO_ROOM, new SimpleEntry(twoRoom_num, twoRoom_price));
		}
		else {
			flatMap.put(FlatType.TWO_ROOM, new SimpleEntry(0, null));
		}
		System.out.print("\nEnter Number of 3-room flats: ");
		int threeRoom_num = sc.nextInt();
		if (threeRoom_num !=0) {
			System.out.print("\nEnter Price of a 3-room flat: ");
			Double threeRoom_price = sc.nextDouble();
			flatMap.put(FlatType.THREE_ROOM, new SimpleEntry(threeRoom_num, threeRoom_price));
		}
		else {
			flatMap.put(FlatType.THREE_ROOM, new SimpleEntry(0, null));
		}
		DateTimeFormatter format = DateTimeFormatter.ofPattern("M/d/yyyy");
		System.out.print("\nEnter Opening Date in M/D/YYYY: ");
		LocalDate openDate = LocalDate.parse(sc.next(), format);
		System.out.print("\nEnter Closing Date in M/D/YYYY: ");
		LocalDate closeDate = LocalDate.parse(sc.next(), format);
		System.out.print("\nEnter number of Officer Slots: ");
		int offSlots = sc.nextInt();
		Project newProj = new Project(name, neighbourhood, flatMap, openDate, closeDate, offSlots);
		newProj.addManager(manager);
	}
	public void viewProject(User user) {
		Project project = null;
		Scanner sc = new Scanner(System.in);
		System.out.print("\nEnter project name you would like to see: ");
		String projName = sc.nextLine();
		project = this.getProject(projName);
		if (project == null) {
			System.out.print("\nProject not found");
		}
		else {
			System.out.print(project.toString());
		}
	}
	public boolean removeProject(String projectName) {
		Iterator<Project> iter = projectList.iterator();
		while (iter.hasNext()) {
			Project proj = iter.next();
			if (proj.getProjectName().equalsIgnoreCase(projectName)) {
				iter.remove();
				return true;
			}
		}
		return false;
	}
	public Project getProject(String projectName) {
		for (Project proj : projectList) {
			if (proj.getProjectName().equalsIgnoreCase(projectName)) {
				return proj;
			}
		}
		return null;
	}
	
	
	public void filterView(FilterSettings filters) {
	    System.out.println("\n=== Filtered Projects ===");
	    for (Project p : projectList) {                 
	        // Filter by location
	        if (filters.getLocation() != null &&
	            !p.getNeighbourhood().equalsIgnoreCase(filters.getLocation())) {
	            continue;
	        }

	        // Filter by flat type
	        if (filters.getFlatType() != null &&
	            !p.getFlatTypes().contains(filters.getFlatType())) {
	            continue;
	        }

	        // Passed all filters
	        System.out.println("- " + p.getProjectName() + " @ " + p.getNeighbourhood());
	    }
	}
}

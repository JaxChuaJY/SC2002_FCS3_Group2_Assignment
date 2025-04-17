package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import enums.FlatType;
import interfaces.IFileHandler;
import interfaces.IProjectManager;
import interfaces.IUserManager;
import user.FilterSettings;
import user.HDBManager;
import user.HDBOfficer;
import user.User;

public class ProjectManager implements IProjectManager {

	private List<Project> projectList;

	private static String directory = "data/";
	private static String projectFileName = "ProjectList.csv";

	public ProjectManager(IFileHandler fileHandler, IUserManager userManager) {
		// list = readProject(ur); // old
		projectList = alt_readProject(fileHandler, userManager);
	}

	//--------FILE READ/WRITE
	//Old one, delete when done, rename the new one also
	public static List<Project> readProject(IUserManager ur) {
		List<Project> list = new ArrayList<Project>();
		try (Scanner scanner = new Scanner(new File(directory + projectFileName))) {
			scanner.useDelimiter(",");

			if (scanner.hasNextLine()) {
				scanner.nextLine(); // Read and discard the first row
			}

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] data = line.split(",");

				if (data.length < 5) {
					throw new Exception("Data dirty -- Missing data");
				}

				Project p = new Project();
				p.setProjectName(data[0]);
				p.setNeighbourhood(data[1]);
				// Selling price for flatType MISSING

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy");
				p.setOpeningDate(LocalDate.parse(data[8], formatter));
				p.setClosingDate(LocalDate.parse(data[9], formatter));

				HDBManager m = (HDBManager) ur.searchUser_Type(HDBManager.class, data[10].replace("\"", ""));
				p.setManager(m);// 10
				m.addProject(p);

				p.setOfficerSlots(Integer.parseInt(data[11]));

				for (int i = 12; i < data.length; i++) {
					HDBOfficer o = (HDBOfficer) ur.searchUser_Type(HDBOfficer.class, data[i].replace("\"", ""));
					p.addOfficer(o);
					o.addProject(p);
				}
				list.add(p);
			}
			// scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find file!");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Error in reading File! -- AGE conversion");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("General Error!");
			e.printStackTrace();
		}

		return list;
	}

	//new one
	public static List<Project> alt_readProject(IFileHandler fileHandler, IUserManager userManager) {

		List<String> allLines = fileHandler.readFromFile(directory + projectFileName);
		List<String> noHeaderLines = allLines.subList(1, allLines.size());
		List<Project> projectList = new ArrayList<Project>();

		DateTimeFormatter format = DateTimeFormatter.ofPattern("M/d/yyyy");
		

		for (String line : noHeaderLines) {
			String[] items = line.split(",");
			EnumMap<FlatType, SimpleEntry<Integer, Double>> flatMap = new EnumMap<>(FlatType.class);
			//Mapping of FlatType with number of units + price
			if (items[2].equalsIgnoreCase("2-room")) {
				flatMap.put(FlatType.TWO_ROOM, new SimpleEntry(Integer.parseInt(items[3]), Double.parseDouble(items[4])));
			}
			else {
				flatMap.put(FlatType.THREE_ROOM, new SimpleEntry(Integer.parseInt(items[3]), Double.parseDouble(items[4])));
			}
			if (items[5].equalsIgnoreCase("2-room")) {
				flatMap.put(FlatType.TWO_ROOM, new SimpleEntry(Integer.parseInt(items[6]), Double.parseDouble(items[7])));
			}
			else {
				flatMap.put(FlatType.THREE_ROOM, new SimpleEntry(Integer.parseInt(items[6]), Double.parseDouble(items[7])));
			}

			String name = items[0];
			String neighbourhood = items[1];
			LocalDate openDate = LocalDate.parse(items[8], format);
			LocalDate closeDate = LocalDate.parse(items[9], format);
			int offSlots = Integer.parseInt(items[11]); // Officer Slots mean empty slots left or total slots?
			Project project = new Project(name, neighbourhood, flatMap, openDate, closeDate, offSlots);
			if (!items[10].isEmpty()) {
				// getManager from manager list using name
				HDBManager m;
				try {
					m = (HDBManager) userManager.searchUser_Type(HDBManager.class, items[10].replace("\"", ""));
					project.setManager(m);// 10
					m.addProject(project);
				} catch (ClassCastException e) {
					// TODO Auto-generated catch block
					System.out.println("Casting error from User to Manager");
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if (!items[12].isEmpty()) {
				String[] offList = items[12].split(",");
				for (int i = 12; i < items.length; i++) {
					// getOfficer from officer manager using name
					HDBOfficer o;
					try {
						o = (HDBOfficer) userManager.searchUser_Type(HDBOfficer.class, items[i].replace("\"", ""));
						project.addOfficer(o);
						o.addProject(project);
					} catch (ClassCastException e) {
						// TODO Auto-generated catch block
						System.out.println("Casting error from User to Officer");
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			projectList.add(project);

		}
		
		return projectList;

	}

	//Missing a writeProject?
	
	//--------FOR REGISTRATION
	public List<Project> getFilteredList_Register(User user) {
		// TODO Auto-generated method stub
		return projectList.stream().filter(p -> projectREGviewCriteria((HDBOfficer) user, p)).collect(Collectors.toList());
	}

	public boolean projectREGviewCriteria(HDBOfficer user, Project project) {

		if (user.getManagedProject().contains(project)) {
			return false;
		}

		if (user.getApplication() != null) {
			if (user.getApplication().getProject().equals(project)) {
				return false;
			}
		}

		for (Project userProject : user.getManagedProject()) {
			if (project.getOpeningDate().isBefore(userProject.getClosingDate())
					|| project.getOpeningDate().isEqual(userProject.getClosingDate())
							&& userProject.getOpeningDate().isBefore(project.getClosingDate())
					|| userProject.getOpeningDate().equals(project.getOpeningDate())) {
				return false;
			}
		}

		return true;
	}

	// REMEMBER TO REMOVE!
	public void printList() {

		System.out.println("============ProjectManager PRINT ALL============");

		for (Project p : projectList) {
			System.out.println("-----------");
			System.out.print(p);
			p.printOfficerList();
			p.printFlatSupply();
			System.out.println("----------");
		}
	}
	
	//--------ProjectManager functions
	public void addProject(HDBManager manager) { 
		//Change User to HDBManager, whoever calls it needs a ClassCastingException try-catch
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
	
	
	//--------GETTER AND SETTERS
	
	
	public Project getProject(String projectName) {
		// TODO Auto-generated method stub
		List<Project> list = this.projectList.stream().filter(p -> p.getProjectName().equals(projectName))
				.collect(Collectors.toList());

		if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new IllegalStateException("Expected exactly one match, but found " + list.size());
		}
	}

	@Override
	public List<Project> getProjectList(User user) {
	    if (user instanceof HDBOfficer) {
	    	HDBOfficer officer = (HDBOfficer) user;
	        return this.projectList.stream()
	                .filter(project -> 
	                officer.getMaritalStatus().canView(project.getFlatTypes(), officer.getAge()) 
                    && LocalDate.now().isBefore(project.getClosingDate())
                    && LocalDate.now().isAfter(project.getOpeningDate())
                    && !officer.getManagedProject().contains(project))
	                .toList();
	    } else {
	        return this.projectList.stream()
	                .filter(project -> 
	                    user.getMaritalStatus().canView(project.getFlatTypes(), user.getAge()) 
	                    && LocalDate.now().isBefore(project.getClosingDate())
	                    && LocalDate.now().isAfter(project.getOpeningDate()))
	                .toList();
	    }
	}

}

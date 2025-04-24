package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
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

/**
 * Manages creation, retrieval, updating, deletion, and filtering of
 * {@link Project} instances.
 * <p>
 * Responsible for reading project data from CSV, writing updates back, and
 * providing project views tailored to different user roles.
 * </p>
 */
public class ProjectManager implements IProjectManager {

	/** In-memory list of all projects. */
	private List<Project> projectList;

	/** Base directory for project CSV file. */
	private static String directory = "data/";

	/** Filename for project data CSV. */
	private static String projectFileName = "ProjectList.csv";

	/**
	 * Constructs the manager, loading projects via the given file and user
	 * handlers.
	 *
	 * @param fileHandler handler for CSV I/O
	 * @param userManager handler for user lookups
	 */
	public ProjectManager(IFileHandler fileHandler, IUserManager userManager) {
		// list = readProject(ur); // old
		projectList = readProject(fileHandler, userManager);
	}

	/**
	 * Reads projects from the CSV file and initializes projectList.
	 *
	 * @param fileHandler handler for reading CSV
	 * @param userManager handler for retrieving User instances
	 * @return list of Project objects
	 */
	public static List<Project> readProject(IFileHandler fileHandler, IUserManager userManager) {

		List<String> allLines = fileHandler.readFromFile(directory + projectFileName);
		List<String> noHeaderLines = allLines.subList(1, allLines.size());
		List<Project> projectList = new ArrayList<Project>();

		DateTimeFormatter format = DateTimeFormatter.ofPattern("M/d/yyyy");

		for (String line : noHeaderLines) {
			String[] items = line.split(",");
			EnumMap<FlatType, SimpleEntry<Integer, Double>> flatMap = new EnumMap<>(FlatType.class);
			// Mapping of FlatType with number of units + price
			if (items[2].equalsIgnoreCase("2-room")) {
				flatMap.put(FlatType.TWO_ROOM,
						new SimpleEntry(Integer.parseInt(items[3]), Double.parseDouble(items[4])));
			} else {
				flatMap.put(FlatType.THREE_ROOM,
						new SimpleEntry(Integer.parseInt(items[3]), Double.parseDouble(items[4])));
			}
			if (items[5].equalsIgnoreCase("2-room")) {
				flatMap.put(FlatType.TWO_ROOM,
						new SimpleEntry(Integer.parseInt(items[6]), Double.parseDouble(items[7])));
			} else {
				flatMap.put(FlatType.THREE_ROOM,
						new SimpleEntry(Integer.parseInt(items[6]), Double.parseDouble(items[7])));
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
				String[] offList = items[12].split("\\|");
				for (int i = 12; i < items.length; i++) {
					// getOfficer from officer manager using name

					HDBOfficer o;

					for (String officerName : offList) {
						String cleanedOfficerName = officerName.replace("\"", "").trim();

						try {
							o = (HDBOfficer) userManager.searchUser_Type(HDBOfficer.class, cleanedOfficerName);
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
			}
			project.setVisible(!LocalDate.now().isBefore(project.getOpeningDate()) && !LocalDate.now().isAfter(project.getClosingDate()));
			projectList.add(project);

		}

		return projectList;

	}

	// write Project List into CSV
	/**
	 * Writes the current projectList to the CSV file.
	 *
	 * @param append if true, preserves header and appends; false overwrites
	 */
	public void writeToCSV(boolean append) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("M/d/yyyy");
		 try (FileWriter writer = new FileWriter(directory+projectFileName, append)) { 
			 if (!append) {
		        	writer.write("Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officer\n");
			 }
		        for (Project proj : projectList) {
		        	EnumMap<FlatType, SimpleEntry<Integer, Double>> flatSupply = proj.getFlatSupply();
		        	
		        	writer.append(proj.getProjectName()).append(",")
					.append(proj.getNeighbourhood()).append(",");
		        	
					for (FlatType type : proj.getFlatTypes()) {
						SimpleEntry<Integer, Double> entry = flatSupply.get(type);
						writer.append(type.toDisplayString()).append(",")
		                		.append(String.valueOf(entry.getKey())).append(",")
		                		.append(String.valueOf(entry.getValue())).append(",");
					}
					writer.append(proj.getOpeningDate().format(format)).append(",")
					.append(proj.getClosingDate().format(format)).append(",")
					.append(proj.getManager().getName().toString()).append(",")
					.append(Integer.toString(proj.getOfficerSlots())).append(",");
					List<String> officerNames = proj.getOfficerList().stream().map(HDBOfficer::getName).toList();
					writer.append(String.join("|", officerNames))
            		.append("\n");
			 
		        }
		        System.out.print("\nProjects CSV File Updated");
		 }	catch (IOException e) {
		        System.err.println("Writing to CSV Error\n");
		    }
	}

	// --------FOR REGISTRATION
	/**
	 * Filters the project list for officer registration eligibility.
	 *
	 * @param user the HDBOfficer applying
	 * @return list of Projects user can register for
	 */
	public List<Project> getFilteredList_Register(User user) {
		// TODO Auto-generated method stub
		return projectList.stream().filter(p -> projectREGviewCriteria((HDBOfficer) user, p))
				.collect(Collectors.toList());
	}

	/**
	 * Criteria for HDBOfficer registration view.
	 *
	 * @param user    the officer
	 * @param project the project to check
	 * @return true if eligible to register
	 */
	public boolean projectREGviewCriteria(HDBOfficer user, Project project) {

		if (user.getManagedProject().contains(project)) {
			//System.out.println("Managing -" + project.getProjectName());
			return false;
		}

		if (user.getApplication() != null) {
			if (user.getApplication().getProject().equals(project)) {
				//System.out.println("Applic -" + project.getProjectName());
				return false;
			}
		}

		for (Project userProject : user.getManagedProject()) {
			if (project.getOpeningDate().isBefore(userProject.getClosingDate())
					|| project.getOpeningDate().isEqual(userProject.getClosingDate())
							&& userProject.getOpeningDate().isBefore(project.getClosingDate())
					|| userProject.getOpeningDate().equals(project.getOpeningDate())) {
				//System.out.println("Deadline -" + project.getProjectName());
				return false;
			}
		}

		return true;
	}

	// --------ProjectManager functions
	/**
	 * Adds a new project via console input. Only accessible by HDBManager.
	 *
	 * @param manager the HDBManager creating the project
	 */
	public void addProject(HDBManager manager) {
		// Change User to HDBManager, whoever calls it needs a ClassCastingException
		// try-catch
		Scanner sc = new Scanner(System.in);
		EnumMap<FlatType, SimpleEntry<Integer, Double>> flatMap = new EnumMap<>(FlatType.class);

		System.out.print("\nEnter name of the Project: ");
		String name = sc.nextLine();
		System.out.print("\nEnter Neighbourhood: ");
		String neighbourhood = sc.nextLine();
		System.out.print("\nEnter Number of 2-room flats: ");
		int twoRoom_num = sc.nextInt();
		if (twoRoom_num != 0) {
			System.out.print("\nEnter Price of a 2-room flat: ");
			Double twoRoom_price = sc.nextDouble();
			flatMap.put(FlatType.TWO_ROOM, new SimpleEntry(twoRoom_num, twoRoom_price));
		} else {
			flatMap.put(FlatType.TWO_ROOM, new SimpleEntry(0, null));
		}
		System.out.print("\nEnter Number of 3-room flats: ");
		int threeRoom_num = sc.nextInt();
		if (threeRoom_num != 0) {
			System.out.print("\nEnter Price of a 3-room flat: ");
			Double threeRoom_price = sc.nextDouble();
			flatMap.put(FlatType.THREE_ROOM, new SimpleEntry(threeRoom_num, threeRoom_price));
		} else {
			flatMap.put(FlatType.THREE_ROOM, new SimpleEntry(0, null));
		}

		DateTimeFormatter format = DateTimeFormatter.ofPattern("M/d/yyyy");
		System.out.print("\nEnter Opening Date in M/D/YYYY: ");
		LocalDate openDate = LocalDate.parse(sc.next(), format);
		System.out.print("\nEnter Closing Date in M/D/YYYY: ");
		LocalDate closeDate = LocalDate.parse(sc.next(), format);
		System.out.print("\nEnter number of Officer Slots: ");
		int offSlots = sc.nextInt();
		while (offSlots > 10) {
			System.out.println("Officer Slot too big, enter again: ");
			offSlots = sc.nextInt();
		}
		Project newProj = new Project(name, neighbourhood, flatMap, openDate, closeDate, offSlots);
		newProj.setVisible(!LocalDate.now().isBefore(newProj.getOpeningDate()) && !LocalDate.now().isAfter(newProj.getClosingDate()));
		newProj.addManager(manager);
		writeToCSV(false);
	}
	
    /**
     * Prompts the user to enter a project name and displays its details.
     * <p>
     * Reads input from console, looks up the project, and prints its information
     * or an error message if not found.
     * </p>
     * @param user the user requesting the project view (used for access control)
     */
	public void viewProject(User user) {
		Project project = null;
		Scanner sc = new Scanner(System.in);
		System.out.print("\nEnter project name you would like to see: ");
		sc.nextLine();
		String projName = sc.nextLine();
		project = this.getProject(projName);
		if (project == null) {
			System.out.print("\nProject not found");
		} else {
			System.out.print(project.toString());
		}
	}

	/**
	 * Displays all projects accessible to the given user.
	 *
	 * @param user the requesting User
	 */
	public void viewAllProj(User user) {
		if (user instanceof HDBManager) {
			for (Project proj : projectList) {
				System.out.print(proj.toString() + "\n");
			}
		}

		else if (user instanceof HDBOfficer) {
			for (Project proj : projectList) {
				if (user.getMaritalStatus().canView(proj.getFlatTypes(), user.getAge()) && proj.getVisibility()) {
					System.out.println(proj.toString() + "\n");
				} else if (proj.getOfficerList().contains((HDBOfficer) user)) {
					System.out.println(proj.toString() + "\n");
				}
			}
		} else {
			for (Project proj : projectList) {
				if (user.getMaritalStatus().canView(proj.getFlatTypes(), user.getAge()) && proj.getVisibility()) {
					System.out.print(proj.toString() + "\n");
				}
			}
		}
	}

	/**
	 * Removes a project by name, updating CSV.
	 *
	 * @param projectName the name to remove
	 * @return true if removed, false otherwise
	 */
	public boolean removeProject(String projectName) {
		Iterator<Project> iter = projectList.iterator();
		while (iter.hasNext()) {
			Project proj = iter.next();
			if (proj.getProjectName().equalsIgnoreCase(projectName)) {
				iter.remove();
				writeToCSV(false);
				return true;
			}
		}
		return false;
	}

	/**
	 * Edits attributes of an existing project via console input.
	 *
	 * @param proj the project to edit
	 */
	public void editProject(Project proj) {
		Scanner sc = new Scanner(System.in);
		int choice;
		System.out.print("\n=== Select Item to edit ===\n");
		System.out.print("1. Name of Project\n");
		System.out.print("2. Neighbourhood Project is located\n");
		System.out.print("3. Change number of 2-Room units\n");
		System.out.print("4. Change pricing of 2-Room units\n");
		System.out.print("5. Change number of 3-Room units\n");
		System.out.print("6. Change pricing of 3-Room units\n");
		System.out.print("7. Change Project application Opening Date\n");
		System.out.print("8. Change Project application Closing Date\n");
		System.out.print("9. Update officer slots for this Project\n");
		choice = sc.nextInt();

		switch (choice) {
		case 1:
			System.out.print("Enter new Project Name: ");
			sc.nextLine();
			proj.setProjectName(sc.nextLine());
			System.out.print("\nName Updated");
			break;
		case 2:
			System.out.print("Enter new Neighbourhood for Project: ");
			sc.nextLine();
			proj.setNeighbourhood(sc.nextLine());
			System.out.print("\nNeighbourhood Updated");
			break;
		case 3:
			System.out.print("Enter new number of 2-Room units: ");
			int newCnt = sc.nextInt();
			EnumMap<FlatType, SimpleEntry<Integer, Double>> flatSupply = proj.getFlatSupply();
			SimpleEntry<Integer, Double> oldEntry = flatSupply.get(FlatType.TWO_ROOM);
			flatSupply.put(FlatType.TWO_ROOM, new SimpleEntry<>(newCnt, oldEntry.getValue()));
			proj.setFlatSupply(flatSupply);
			System.out.print("\n2-Room Unit Count Updated");
			break;
		case 4:
			System.out.print("Enter new Price for 2-Room units (with decimal point): ");
			double newPrice = sc.nextDouble();
			flatSupply = proj.getFlatSupply();
			oldEntry = flatSupply.get(FlatType.TWO_ROOM);
			flatSupply.put(FlatType.TWO_ROOM, new SimpleEntry<>(oldEntry.getKey(), newPrice));
			proj.setFlatSupply(flatSupply);
			System.out.print("\n2-Room Price Updated");
			break;
		case 5:
			System.out.print("Enter new number of 3-Room units: ");
			newCnt = sc.nextInt();
			flatSupply = proj.getFlatSupply();
			oldEntry = flatSupply.get(FlatType.THREE_ROOM);
			flatSupply.put(FlatType.THREE_ROOM, new SimpleEntry<>(newCnt, oldEntry.getValue()));
			proj.setFlatSupply(flatSupply);
			System.out.print("\n3-Room Unit Count Updated");
			break;
		case 6:
			System.out.print("Enter new Price for 3-Room units (with decimal point): ");
			newPrice = sc.nextDouble();
			flatSupply = proj.getFlatSupply();
			oldEntry = flatSupply.get(FlatType.THREE_ROOM);
			flatSupply.put(FlatType.THREE_ROOM, new SimpleEntry<>(oldEntry.getKey(), newPrice));
			proj.setFlatSupply(flatSupply);
			System.out.print("\n3-Room Price Updated");
			break;
		case 7:
			System.out.print("Enter new Opening Date (yyyy-MM-dd): ");
			sc.nextLine();
			String openDateStr = sc.nextLine();
			LocalDate openDate = LocalDate.parse(openDateStr);
			proj.setOpeningDate(openDate);
			System.out.print("\nOpening Date Updated");
			break;
		case 8:
			System.out.print("Enter new Closing Date (yyyy-MM-dd): ");
			sc.nextLine();
			String closeDateStr = sc.nextLine();
			LocalDate closeDate = LocalDate.parse(closeDateStr);
			proj.setClosingDate(closeDate);
			System.out.print("\nClosing Date Updated");
			break;
		case 9:
			System.out.print("Enter number of officer slots to update: ");
			int officerSlots = sc.nextInt();
			proj.setOfficerSlots(officerSlots);
			System.out.print("\nOfficer Slots Updated");
			break;
		default:
			System.out.print("\nInvalid Choice");
			break;
		}
		writeToCSV(false);
	}

	/**
	 * Toggles visibility of a project and persists change.
	 *
	 * @param proj the project to toggle
	 */
	public void toggleVisibility(Project proj) {
		proj.toggleVisibility();
	}

	// Only used by the Manager
	/**
	 * Displays filtered projects based on manager's filter settings.
	 *
	 * @param filters the filter criteria
	 * @param user    the HDBManager applying filters
	 */
	public void filterView(FilterSettings filters, HDBManager user) {
		System.out.println("\n=== Filtered Projects ===");
		for (Project p : projectList) {
			// Filter by location
			if (filters.getLocation() != null && !p.getNeighbourhood().equalsIgnoreCase(filters.getLocation())) {
				continue;
			}

			// Filter by flat type
			if (filters.getFlatType() != null && !p.getFlatTypes().contains(filters.getFlatType())) {
				continue;
			}

			// See all regardless if managing it?
			if (!filters.getmanagerViewALL() && p.getManager().equals((HDBManager) user)) {
				continue;
			}

			// Passed all filters
			System.out.println("- " + p.getProjectName() + " @ " + p.getNeighbourhood());
		}
	}

	// --------GETTER AND SETTERS
	/**
	 * Searches for and returns a single project by name.
	 *
	 * @param projectName the name to search
	 * @return matching Project
	 * @throws IllegalStateException if zero or multiple matches
	 */
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

	/**
	 * Provides projects viewable by the user for applying.
	 *
	 * @param user the User requesting projects
	 * @return filtered list of Projects
	 */
	@Override
	public List<Project> getProjectList(User user) {
		if (user instanceof HDBManager) {
			return this.projectList;
		} else if (user instanceof HDBOfficer) {
			HDBOfficer officer = (HDBOfficer) user;
			return this.projectList.stream()
					.filter(project -> officer.getMaritalStatus().canView(project.getFlatTypes(), officer.getAge())
							&& LocalDate.now().isBefore(project.getClosingDate())
							&& LocalDate.now().isAfter(project.getOpeningDate())
							&& !officer.getManagedProject().contains(project))
					.toList();
		} else {
			return this.projectList.stream()
					.filter(project -> user.getMaritalStatus().canView(project.getFlatTypes(), user.getAge())
							&& LocalDate.now().isBefore(project.getClosingDate())
							&& LocalDate.now().isAfter(project.getOpeningDate()))
					.toList();
		}
	}

	/**
	 * Updates flat room counts and persists to CSV.
	 *
	 * @param project  target project
	 * @param flatType type of flat to adjust
	 * @param x        delta units (positive/negative)
	 * @return true if update succeeded
	 */
	public boolean updateFlatRooms(Project project, FlatType flatType, int x) { // x is the number to increase /
																				// decrease the "X-ROOMS" number (1 /
																				// -1)
		boolean reserved = project.updateFlatSupply(flatType, x);

		try {
			List<String> newlist = new ArrayList<>();
			BufferedReader reader = new BufferedReader(new FileReader(directory + projectFileName));

			System.out.println("Writing to: " + (directory + projectFileName));
			String line;

			while ((line = reader.readLine()) != null) {
				String[] items = line.split(",");
				if (items[0].equalsIgnoreCase(project.getProjectName())) {
					for (int i = 2; i < items.length - 2; i += 3) {
						String typeStr = items[i].trim();
						if (typeStr.equalsIgnoreCase(flatType.toDisplayString())) {
							int currCount = Integer.parseInt(items[i + 1].trim());
							items[i + 1] = String.valueOf(currCount + x);
							break;
						}
					}
					newlist.add(String.join(",", items));
				} else {
					// Unchanged line
					newlist.add(line);
				}
			}
			reader.close();

			BufferedWriter writer = new BufferedWriter(new FileWriter(directory + projectFileName));
			for (String entry : newlist) {
				writer.write(entry);
				writer.newLine();
			}
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return reserved;
	}

}

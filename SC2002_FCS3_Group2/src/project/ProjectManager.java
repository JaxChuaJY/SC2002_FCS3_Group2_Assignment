package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import enums.FlatType;
import interfaces.IProjectManager;
import interfaces.IUserManager;
import user.HDBManager;
import user.HDBOfficer;
import user.User;

public class ProjectManager implements IProjectManager {

	private List<Project> list;

	private static String directory = "data/";
	private static String projectFileName = "ProjectList.csv";
	
	
	public ProjectManager(IUserManager ur) {
		list = readProject(ur);
	}

	
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
				p.setFlatSupply(Map.of(FlatType.TWO_ROOM, Integer.parseInt(data[3]), FlatType.THREE_ROOM,
						Integer.parseInt(data[6])));
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
	
	public List<Project> getFilteredList(User user) {
		// TODO Auto-generated method stub
		return list.stream().filter(p -> projectREGviewCriteria((HDBOfficer) user, p)).collect(Collectors.toList());
	}

	public Project getProject(String projectName) {
		// TODO Auto-generated method stub
		List<Project> list = this.list.stream()
				.filter(p -> p.getProjectName().equals(projectName))
				.collect(Collectors.toList());
		
		if (list.size() == 1) {
		    return list.get(0);
		} else {
		    throw new IllegalStateException("Expected exactly one match, but found " + list.size());
		}
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

		for (Project p : list) {
			System.out.println("-----------");
			System.out.print(p);
			p.printOfficerList();
			p.printFlatSupply();
			System.out.println("----------");
		}
	}

}
package registration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import enums.ApplicationStatus;
import interfaces.IProjectManager;
import interfaces.IProjectRegistration;
import interfaces.IUserManager;
import main.BTOManagementSystem;
import project.Project;
import user.HDBManager;
import user.HDBOfficer;


/**
 * Implements officer registration workflows for BTO projects.
 * <p>
 * Manages submission, approval, and rejection of registration forms.
 * </p>
 */
public class ProjectRegistration implements IProjectRegistration{
	    

	/** Internal list of all registration forms. */
	List<RegistrationForm> list;
	
	/** Base directory for data CSV files. */
	private static String directory = "data/";
	
	/** Filename for project data CSV. */
	private static String projectFileName = "ProjectList.csv";
	
    /** Filename for registration data CSV. */
	private static String registrationFileName = "RegistrationList.csv";
	
	/**
     * Constructs the manager, loading projects via the given file and user handlers.
     *
     * @param ur handler for user lookups
     * @param pr handler for project lookups
     */
	public ProjectRegistration(IUserManager ur, IProjectManager pr){
		list = readRegForms(ur, pr);
	}
	
	/**
     * Reads registration forms from CSV, creating RegistrationForm instances.
     *
     * @param ur user manager for officer lookups
     * @param pr project manager for project lookups
     * @return list of RegistrationForm loaded
     */
	public static List<RegistrationForm> readRegForms(IUserManager ur, IProjectManager pr) {
		List<RegistrationForm> list = new ArrayList<RegistrationForm>();

		try (Scanner sc = new Scanner(new File(directory + registrationFileName))) {
			sc.useDelimiter(",");

			if (sc.hasNextLine()) {
				sc.nextLine();
			}

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] data = line.split(",");

				RegistrationForm f = new RegistrationForm();
				f.setRegisteredBy((HDBOfficer) ur.searchUser_Type(HDBOfficer.class, data[0]));
				f.setProject(pr.getProject(data[1]));
				f.setStatus(data[2]);

				list.add(f);
			}

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
	
	/**
     * Appends a new registration entry to the CSV file.
     *
     * @param p project registered for
     * @param u officer submitting the registration
     */
	public void writeFile_newRegForm(Project p, HDBOfficer u) {

		String newRow = u.getName() + "," + p.getProjectName() + "," + ApplicationStatus.PENDING.toString();
		List<String> lines = new ArrayList<>();

		try (Scanner scanner = new Scanner(new File(directory + registrationFileName))) {
			scanner.useDelimiter(",");
			while (scanner.hasNextLine()) {
				lines.add(scanner.nextLine() + "\n");
			}

			lines.add(newRow);
			scanner.close();

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(directory + registrationFileName))) {
				for (String line : lines) {
					writer.write(line);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
     * Updates an existing form's status in the CSV file.
     *
     * @param f the RegistrationForm to persist
     */
	public void writeFile_setRegForm(RegistrationForm f) {

		List<String> lines = new ArrayList<>();

		try (Scanner scanner = new Scanner(new File(directory + registrationFileName))) {
			scanner.useDelimiter(",");

			lines.add(scanner.nextLine() + "\n");

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] data = line.split(",");

				// Does it contain the NRIC?
				if (data[0].equals(f.getRegisteredBy().getName()) && data[1].equals(f.getProject().getProjectName())) {
					lines.add(data[0] + "," + data[1] + "," + f.getStatus().toString() + "\n");
				} else {
					lines.add(data[0] + "," + data[1] + "," + data[2] + "\n");
				}
			}
			scanner.close();

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(directory + registrationFileName))) {
				for (String line : lines) {
					writer.write(line);
				}
			}
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

	}

	/**
     * Adds an officer name to the project CSV when registration is approved.
     *
     * @param f the RegistrationForm containing officer and project
     */
	public void writeFile_addOfficer(RegistrationForm f) {
		List<String> lines = new ArrayList<>();

		try (Scanner scanner = new Scanner(new File(directory + projectFileName))) {
			scanner.useDelimiter(",");

			lines.add(scanner.nextLine() + "\n");

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] data = line.split(",");

				if (data[0].equals(f.getProject().getProjectName())) {
					lines.add(line.substring(0, line.length()-1) + "," + f.getRegisteredBy().getName() + "\"\n");
				} else {
					lines.add(line + "\n");
				}
			}
			scanner.close();

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(directory + projectFileName))) {
				for (String line : lines) {
					writer.write(line);
				}
			}
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
	}
	
	/**
     * Submits a new registration form.
     *
     * @param project the project to register for
     * @param user    the HDBOfficer submitting the form
     */
	public void register(Project project, HDBOfficer user) {
		list.add(new RegistrationForm(project, user));
		writeFile_newRegForm(project, user);
	}
	
	/**
     * Approves a registration form.
     *
     * @param form the RegistrationForm to approve
     */
	public void approveRegistration(RegistrationForm form) {
		form.approveStatus();
	}
	
	/**
     * Rejects a registration form.
     *
     * @param form the RegistrationForm to reject
     */
	public void rejectRegistration(RegistrationForm form) {
		form.rejectStatus();
	}
	

	 /**
     * Prints pending and approved registrations for a manager.
     *
     * @param user the HDBManager to filter registrations for
     */
	public void printList_ManagerFilter(HDBManager user) {
		
		System.out.println("============List of Pending/Approved Registrations============");
		
		for (RegistrationForm r: list) {
			if (r.getProject().getManager().equals(user) && (r.getStatus().equals(ApplicationStatus.PENDING) 
					|| r.getStatus().equals(ApplicationStatus.SUCCESSFUL) )) {
				System.out.println(r);
			}
		}
		
		System.out.println("============================END==============================");
	}
	
	/**
     * Prints all registration forms submitted by an officer.
     *
     * @param u the HDBOfficer whose forms to print
     */
	public void printList_officer(HDBOfficer u) {
		System.out.println("=====Registration form of User=====");
		for (RegistrationForm r: list) {
			if (r.getRegisteredBy().equals(u)) {
				System.out.println(r);
			}
		}
		System.out.println("===============END================");
	}
	
	/**
     * Retrieves pending registration forms for a manager to review.
     *
     * @param u the HDBManager whose projects to filter
     * @return list of pending RegistrationForm
     */
	public List<RegistrationForm> getFilteredList(HDBManager u) {
		return list.stream()
				.filter(r ->
						u.getManagedProject().contains(r.getProject())
						&& r.getStatus() == ApplicationStatus.PENDING)
				.collect(Collectors.toList());
	}
	
	/**
     * Returns projects not yet registered for by the officer or pending.
     *
     * @param l list of all projects
     * @param u the HDBOfficer to filter for
     * @return list of Projects available for registration
     */
	public List<Project> getNonRegList(List<Project> l, HDBOfficer u){
		
		List<Project> userProjects = list.stream()
		        .filter(r -> r.getRegisteredBy().equals(u) || r.getStatus().equals(ApplicationStatus.PENDING))
		        .map(RegistrationForm::getProject) 
		        .collect(Collectors.toList());

		return l.stream()
			    .filter(project -> !userProjects.contains(project))
			    .collect(Collectors.toList());
	}
	 
	/**
     * @return a copy of the internal registration list
     */
	public List<RegistrationForm> getList() {
		return list;
	}
	
	
}

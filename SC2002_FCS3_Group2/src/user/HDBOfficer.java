package user;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import enums.MaritalStatus;
import interfaces.IOfficerRegister;
import main.BTOManagementSystem;
import project.Project;

/**
 * Represents an HDB Officer in the BTO Management System.
 * <p>
 * HDB Officers can apply for projects, manage applications, handle enquiries,
 * register for projects as officers, and navigate officer-specific menus.
 * </p>
 */
public class HDBOfficer extends Applicant implements IOfficerRegister {
	
	/**
     * List of projects this officer is assigned to manage.
     */
	private List<Project> managedProject = new ArrayList<Project>();;

	/**
     * Constructs a new HDBOfficer with personal and login details.
     *
     * @param name           the officer's name
     * @param nric           the officer's NRIC identifier
     * @param age            the officer's age
     * @param maritalstatus  the officer's marital status
     * @param password       the officer's login password
     */
	public HDBOfficer(String name, String nric, int age, MaritalStatus maritalstatus, String password) {
		super(name, nric, age, maritalstatus, password);
		// TODO Auto-generated constructor stub
	}

	/**
     * Displays the Officer menu and handles user selections.
     * <p>
     * Options include changing password, project section, application section,
     * enquiry menu, registration section, and exit.
     * </p>
     *
     * @param btoSys the BTOManagementSystem instance for delegating actions
     */
	@Override
	public void showMenu(BTOManagementSystem btoSys) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		int choice;
		do {
			System.out.println("======Officer MENU=======");
			System.out.println("1. Change Password");
			System.out.println("2. Project section");
			System.out.println("3. Application Section");
			System.out.println("4. Enquiries menu");
			System.out.println("5. Registration Section");
			System.out.println("6. EXIT");
			try {
				choice = sc.nextInt();

				switch (choice) {
				case 1:
					btoSys.changePassword();
					btoSys.login();
					return;
				case 2: 
					btoSys.showProjMenu();
					break;
				case 3:
					btoSys.showApplMenu();
					break;
				case 4:
					btoSys.showEnquiryMenu();
					break;
				case 5:
					btoSys.showRegMenu();
					break;
				case 6:
					btoSys.logout();
					return;
				default:
					System.out.println("Input out of range");
				}
			} catch (InputMismatchException e) {
				System.out.println("Please input a integer.");
				sc.nextLine();
				e.printStackTrace();
			}catch (Exception e) {
				System.out.println("General error");
				e.printStackTrace();
			}

		} while (true);

	}

	/**
     * Adds a project to this officer's management list.
     *
     * @param p the Project to assign
     */
	public void addProject(Project p) {
		managedProject.add(p);
		
		/* missing from git
		 * boolean hasOverlap = managedProject.stream()
		        .anyMatch(entry ->
		            !(entry.getClosingDate().isBefore(project.getOpeningDate())
		            || entry.getOpeningDate().isAfter(project.getClosingDate()))
		        );
		 * */
	}

	/**
     * Registers this officer for project assignments (stub).
     */
	@Override
	public void registerProject() {
		// TODO Auto-generated method stub

	}

    /**
     * Returns a summary of this officer's registration (stub).
     *
     * @return registration details as a String
     */
	@Override
	public String viewRegistration() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     * Retrieves the list of projects managed by this officer.
     *
     * @return list of Project instances
     */
	public List<Project> getManagedProject() {
		return managedProject;
	}

	/**
     * Replaces this officer's managed project list.
     *
     * @param managedProject new list of projects
     */
	public void setManagedProject(List<Project> managedProject) {
		this.managedProject = managedProject;
	}

	/**
     * Alias for getManagedProject(), returns the projects managed.
     *
     * @return list of Project instances
     */
	public List<Project> getProjects() {
		// TODO Auto-generated method stub
		return managedProject;
	}
}

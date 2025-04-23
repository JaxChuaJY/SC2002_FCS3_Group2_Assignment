package user;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import enums.MaritalStatus;
import main.BTOManagementSystem;
import project.Project;

/**
 * Represents an HDB Manager in the BTO Management System.
 * <p>
 * HDB Managers can create and manage BTO projects, process applications,
 * handle registrations, generate reports, and navigate manager-specific menus.
 * </p>
 */
public class HDBManager extends User {
	
    /** List of projects this manager is responsible for. */
	private List<Project> managedProject = new ArrayList<Project>();

	/**
     * Constructs a new HDBManager with personal and login details.
     *
     * @param name           the manager's name
     * @param nric           the manager's NRIC identifier
     * @param age            the manager's age
     * @param maritalstatus  the manager's marital status (unused for HDBManager)
     * @param password       the manager's login password
     */
	public HDBManager(String name, String nric, int age, MaritalStatus maritalstatus, String password) {
		super(name, nric, age, maritalstatus, password);
		// TODO Auto-generated constructor stub
	}

	/**
     * Displays the Manager menu and handles user selections.
     * <p>
     * Options include changing password, project section, application section,
     * enquiry menu, registration section, report generation, and exit.
     * </p>
     *
     * @param btoSys the BTOManagementSystem instance for delegating actions
     */
	@Override
	public void showMenu(BTOManagementSystem btoSys) {
		Scanner sc = new Scanner(System.in);
		int choice;
		do {
			System.out.println("======Manager MENU=======");
			System.out.println("1. Change Password");
			System.out.println("2. Project section");
			System.out.println("3. Application Section");
			System.out.println("4. Enquiries menu");
			System.out.println("5. Registration Section");
			System.out.println("6. Generate report");
			System.out.println("7. Filter settings");
			System.out.println("8. EXIT");
			choice = sc.nextInt();

			try {
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
					btoSys.showReportMenu();
 					break;
				case 7:
					btoSys.showFilterMenu();
 					break;
				case 8:
					btoSys.logout();
					return;
				default:
					System.out.println("Input out of range");

				}
			} catch (InputMismatchException e) {
				System.out.println("Please input a integer.");
				sc.nextLine();
			} catch (Exception e) {
				System.out.println("General error");
				e.printStackTrace();
			}

		} while (true);

	}

	/**
     * Adds a project to this manager's responsibility list.
     *
     * @param p the Project to add
     */
	public void addProject(Project p) {
		managedProject.add(p);
	}

	/**
	 * @return the managedProject
	 */
	public List<Project> getManagedProject() {
		return managedProject;
	}

	/**
	 * @param managedProject the managedProject to set
	 */
	public void setManagedProject(List<Project> managedProject) {
		this.managedProject = managedProject;
	}

}

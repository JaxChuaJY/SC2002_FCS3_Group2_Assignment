package user;

import java.util.InputMismatchException;
import java.util.Scanner;

import application.Application;
import enums.MaritalStatus;
import main.BTOManagementSystem;

/**
 * Represents an Applicant in the BTO Management System.
 * <p>
 * An Applicant can view and apply for BTO projects,
 * manage their submitted application, and navigate through
 * Applicant-specific menus.
 * </p>
 */
public class Applicant extends User {

	/** The current BTO application for this applicant. */
	Application application;

	/**
     * Constructs a new Applicant with personal credentials.
     *
     * @param name           the applicant's name
     * @param nric           the applicant's NRIC identifier
     * @param age            the applicant's age
     * @param maritalstatus  the applicant's marital status
     * @param password       the applicant's login password
     */
	public Applicant(String name, String nric, int age, 
			MaritalStatus maritalstatus, String password){
		super(name, nric, age, maritalstatus, password);
	}

	/**
     * Retrieves this applicant's current BTO application.
     *
     * @return the Application object, or null if none submitted
     */
	public Application getApplication() {
		return application;
	}

    /**
     * Sets or updates this applicant's BTO application.
     *
     * @param application the Application to assign to this applicant
     */
	public void setApplication(Application application) {
		this.application = application;
	}

	/**
     * Displays the Applicant menu and handles user interactions.
     * <p>
     * Options include changing password, project browsing,
     * application management, enquiry handling, and exit.
     * </p>
     *
     * @param btoSys the BTOManagementSystem instance for delegating actions
     */
	@Override
	public void showMenu(BTOManagementSystem btoSys) {
		Scanner sc = new Scanner(System.in);
		int choice;
		do {
			System.out.println("======Applicant MENU=======");
			System.out.println("1. Change Password");
			System.out.println("2. Project section");
			System.out.println("3. Application Section");
			System.out.println("4. Enquiries menu");
			System.out.println("5. EXIT");
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
}

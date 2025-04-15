package user;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import enums.MaritalStatus;
import main.BTOManagementSystem;
import project.Project;

public class HDBManager extends User {
	
	private List<Project> managedProject = new ArrayList<Project>();

	public HDBManager(String name, String nric, int age, MaritalStatus maritalstatus, String password) {
		super(name, nric, age, maritalstatus, password);
		// TODO Auto-generated constructor stub
	}

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
			System.out.println("7. EXIT");
			choice = sc.nextInt();

			try {
				switch (choice) {
				case 1:
					btoSys.changePassword();
					break;
				case 2:
					btoSys.showProjMenu();
					break;
				case 3:
				case 4:
				case 5:
					btoSys.showRegMenu();
					break;
				case 6:
				case 7:
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

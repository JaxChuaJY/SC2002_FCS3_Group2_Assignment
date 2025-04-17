package user;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import enums.MaritalStatus;
import interfaces.IOfficerRegister;
import main.BTOManagementSystem;
import project.Project;

public class HDBOfficer extends Applicant implements IOfficerRegister {
	
	private List<Project> managedProject = new ArrayList<Project>();;

	
	public HDBOfficer(String name, String nric, int age, MaritalStatus maritalstatus, String password) {
		super(name, nric, age, maritalstatus, password);
		// TODO Auto-generated constructor stub
	}

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

	@Override
	public void registerProject() {
		// TODO Auto-generated method stub

	}

	@Override
	public String viewRegistration() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Project> getManagedProject() {
		return managedProject;
	}

	public void setManagedProject(List<Project> managedProject) {
		this.managedProject = managedProject;
	}

	public List<Project> getProjects() {
		// TODO Auto-generated method stub
		return managedProject;
	}
}

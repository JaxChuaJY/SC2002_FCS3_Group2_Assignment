package user;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import interfaces.IOfficerRegister;
import main.BTOManagementSystem;
import project.Project;

public class HDBOfficer extends Applicant implements IOfficerRegister {
	private List<Project> managedProject = new ArrayList<Project>();;

	// REMOVE
	public HDBOfficer(String n, int age, String nric) {
		super.setName(n);
		super.setAge(age);
		super.setNric(nric);
		super.setMaritalStatus(MaritalStatus.SINGLE);
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
					break;
				case 2: 
				case 3:
				case 4:
				case 5:
					btoSys.getProjectRegManager().regSection_Officer(btoSys);
					break;
				case 6:
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

	public HDBOfficer() {

	}

	public void addProject(Project p) {
		managedProject.add(p);
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

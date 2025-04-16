package user;

import java.util.InputMismatchException;
import java.util.Scanner;

import application.Application;
import application.ApplicationManager;
import enums.ApplicationStatus;
import enums.MaritalStatus;
import interfaces.IApplicantAction;
import main.BTOManagementSystem;

public class Applicant extends User implements IApplicantAction {
	Application application;

	public Applicant(String name, String nric, int age, 
			MaritalStatus maritalstatus, String password){
		super(name, nric, age, maritalstatus, password);
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

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
				case 4:
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

	@Override
	public void applyForProject(Application a) {
		this.application = a;

	}

	@Override
	public void removeApplication() {
		this.application = null;
	}

	@Override
	public void withdrawApplication(ApplicationManager aM) {
		aM.getApplicationList().values()
				.forEach(set -> set.stream().filter(application -> application.getApplicant().equals(this))
						.forEach(application -> application.setStatus(ApplicationStatus.WITHDRAW_REQUEST)));
		System.out.println("Application withdrawn request successful.");
		this.application.setStatus(ApplicationStatus.WITHDRAW_REQUEST);
	}

	@Override
	public String viewApplicationStatus() {
		return this.application.getStatus().toString();	
	}

}

package registration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import enums.ApplicationStatus;
import interfaces.IProjectRegistration;
import main.BTOManagementSystem;
import project.Project;
import user.HDBManager;
import user.HDBOfficer;

public class ProjectRegistration implements IProjectRegistration{
	List<RegistrationForm> list;

	//REMOVE?
	public ProjectRegistration(){
		list = new ArrayList<RegistrationForm> ();
	}
	
	public ProjectRegistration(List<RegistrationForm> l){
		list = l;
	}
	
	public void regSection_Officer(BTOManagementSystem btoSys) {

		Scanner sc = new Scanner(System.in);
		int choice;

		if (btoSys.getUserManager().getcurrentUser() instanceof HDBOfficer) {
			choice = -1;
			HDBOfficer user = (HDBOfficer) btoSys.getUserManager().getcurrentUser();
			do {
				System.out.println("**--Officer Registration Page");
				System.out.println("1. Register for Project");
				System.out.println("2. View Registration Status");
				System.out.println("3. Exit");
				choice = sc.nextInt();

				switch (choice) {
				case 1: 
					do {
						List<Project> filteredList = btoSys.filterListRegister();

						int i = 1;
						for (Project p : filteredList) {
							System.out.println(i++ + ". " + p);
						}
						System.out.println(i + ". Exit");
						System.out.println("Select a project by number: ");
						choice = sc.nextInt();

						if (choice >= 1 && choice <= filteredList.size()) {
							Project selected = filteredList.get(choice - 1);
							System.out.println("Registering for: " + selected.getProjectName());
							btoSys.newRegisterProject(selected);
							return;

						} else if (choice == i) {
							return;
						} else {
							System.out.println("Invalid selection.");
						}
					} while (true);

				case 2: // Prints all registration form of user
					System.out.println("Printing all registration forms");
					btoSys.getProjectRegManager().printList_officer(user);
					return;

				case 3:
					System.out.println("Exitting Registration Page...");
					break;
				default:
					System.out.println("INVALID INPUT");
					break;

				}
			} while (choice != 3);

		} else {
			System.out.println("EXIT!");
		}
		return;
	}

	public void regSection_Manager(BTOManagementSystem btoSys) {
		Scanner sc = new Scanner(System.in);
		int choice = -1;
		HDBManager user = (HDBManager) btoSys.getUserManager().getcurrentUser();

		do {
			System.out.println("**--Manager Registration Page");
			System.out.println("1. Review pending registration applications");
			System.out.println("2. Exit");

			choice = sc.nextInt();

			switch (choice) {
			case 1:
				do {
					List<RegistrationForm> filteredList = btoSys.getProjectRegManager().getFilteredList(user);

					int i = 1;
					for (RegistrationForm r : filteredList) {
						System.out.println(i++ + ". " + r);
					}
					System.out.println(i + ". Exit");
					System.out.println("Select a project by number: ");
					choice = sc.nextInt();

					if (choice >= 1 && choice <= filteredList.size()) {
						RegistrationForm selected = filteredList.get(choice - 1);

						do {
							System.out.println("Approve/Reject: " + selected.getProject().getProjectName() + " for "
									+ selected.getRegisteredBy());

							System.out.println("Yes or No (Y/N)?");
							String input = sc.next();

							if (input.toLowerCase().equals("y")) {
								if (selected.getProject().checkOfficerSlotCap()) {
									selected.approveStatus();
									System.out.println("Form approved!");			
								} else {
									System.out.println("Form not approved, full!");
									System.out.println("Automatic rejection...");
									selected.rejectStatus();
								}
								
								
								btoSys.setRegistrationStatus(selected);
								//Add into officer also
								return;
							} else if (input.toLowerCase().equals("n")) {
								selected.rejectStatus();
								System.out.println("Form rejected!");
								btoSys.setRegistrationStatus(selected);
								return;
							} else {
								System.out.println("Input error");
							}
							

						} while (true);

					} else if (choice == i) {
						return;
					} else {
						System.out.println("Invalid selection.");
					}

				} while (true);
			case 2:
				System.out.println("Exitting Registration Page...");
				return;
			default:
				System.out.println("INVALID INPUT");
			}

		} while (true);

	}
	
	
	public void register(Project project, HDBOfficer user) {
		list.add(new RegistrationForm(project, user));
	}
	
	public void approveRegistration(RegistrationForm form) {
		form.approveStatus();
	}
	
	public void rejectRegistration(RegistrationForm form) {
		form.rejectStatus();
	}
	
	//REMOVE -- For checking
	public void printList() {
		
		System.out.println("============ProjectRegistration PRINT ALL============");
		
		for (RegistrationForm r: list) {
			System.out.println(r);
		}
	}
	
	public void printList_officer(HDBOfficer u) {
		for (RegistrationForm r: list) {
			if (r.getRegisteredBy().equals(u)) {
				System.out.println(r);
			}
		}
	}
	
	public List<RegistrationForm> getFilteredList(HDBManager u) {
		return list.stream()
				.filter(r ->
						u.getManagedProject().contains(r.getProject())
						&& r.getStatus() == ApplicationStatus.WAITING)
				.collect(Collectors.toList());
	}
	
	public List<Project> getNonRegList(List<Project> l, HDBOfficer u){
		
		List<Project> userProjects = list.stream()
		        .filter(r -> r.getRegisteredBy().equals(u))
		        .map(RegistrationForm::getProject) 
		        .collect(Collectors.toList());

		return l.stream()
			    .filter(project -> !userProjects.contains(project))
			    .collect(Collectors.toList());
	}
	
	public List<RegistrationForm> getList() {
		return list;
	}
	
	
}

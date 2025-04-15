package main;


import java.util.List;
import java.util.Scanner;

import application.Application;
import application.ApplicationManager;
import interfaces.IApplicationManager;
import interfaces.IFileHandler;
import interfaces.IProjectManager;
import interfaces.IUserManager;
import project.Project;
import project.ProjectManager;
import registration.ProjectRegistration;
import registration.RegistrationForm;
import user.Applicant;
import user.HDBManager;
import user.HDBOfficer;
import user.UserManager;

public class BTOManagementSystem {
	private IUserManager userManager;
    private IProjectManager projectManager;
    private ProjectRegistration projectRegManager; //interface this !
    private final IApplicationManager applicationManager;
	private final IFileHandler fileHandler;
	
	
	BTOManagementSystem(){
		fileHandler = new FileHandler();
		userManager = new UserManager(fileHandler);
		projectManager = new ProjectManager(fileHandler, userManager);
		projectRegManager = new ProjectRegistration(userManager, projectManager);
		applicationManager = new ApplicationManager(projectManager,userManager,fileHandler);
	}
	
	public void startSystem() {
		userManager.printAllUser();
		login();
        if (userManager.getcurrentUser() != null) {
        	System.out.println(userManager.getcurrentUser());
        } else {
            System.out.println("Login failed. Exiting system.");
        }
	}
	
	private void login() {
		do {
			userManager.login();
		} while (userManager.getcurrentUser() == null);
	}
	
	public void changePassword() { 
		userManager.changePassword();
		userManager.logout();
		
		try {
			userManager.reIntialise();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		login();
		
	}
	
	public void showMenu() {
		userManager.getcurrentUser().showMenu(this);
	}
	
	//-----Project Section
	/*
	 * Feel free to do it as you like
	 */
	
	public void showProjMenu() {
		/*
		 * Applicant + Officer
		 * 	1. View Filtered List and select a Project
		 * 	2. Project details is shown, ask if want to apply
		 * 
		 * Manager
		 * 	1. Choose to view List or Create new Project
		 * 	2. (View) View Filtered List and select a Project
		 * 	3. (View) Project details is shown, Edit/Delete/Toggle
		 *  2. (Create) Create... lol
		 */
	}
	
	
	//-----Register Section
	public void showRegMenu() { 
		if (userManager.getcurrentUser() instanceof HDBOfficer) {
			regSection_Officer(this);
		} else if (userManager.getcurrentUser() instanceof HDBManager) {
			regSection_Manager(this);
		}
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
								btoSys.addOfficerToProj(selected);
								projectRegManager.writeFile_setRegForm(selected);
								projectRegManager.writeFile_addOfficer(selected);
								return;
							} else if (input.toLowerCase().equals("n")) {
								selected.rejectStatus();
								System.out.println("Form rejected!");
								projectRegManager.writeFile_setRegForm(selected);
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
	
	public void newRegisterProject(Project p) {
		projectRegManager.register(p, (HDBOfficer)userManager.getcurrentUser());
	}
	
	public List<Project> filterListRegister(){
		List<Project> filteredList = projectManager.getFilteredList_Register(userManager.getcurrentUser());
		return projectRegManager.getNonRegList(filteredList, (HDBOfficer) userManager.getcurrentUser());
	}
	
	public void addOfficerToProj(RegistrationForm f) {
		f.getProject().addOfficer(f.getRegisteredBy());
		f.getRegisteredBy().addProject(f.getProject());	
	}
	
	//-----Application section
	public void showApplMenu() {
		if (userManager.getcurrentUser() instanceof HDBManager) {
			showMenuManager((HDBManager) userManager.getcurrentUser());
		} else if (userManager.getcurrentUser() instanceof HDBOfficer) {
			//showMenuOfficer((HDBOfficer) userManager.getcurrentUser());
		}else if (userManager.getcurrentUser() instanceof Applicant) {
			showMenuApplicant( (Applicant) userManager.getcurrentUser());
		}
		
	}
	
	//Application Menu section
	/*
	 * For Officer and Manager navigation:
	 * 	1. Views all Project they are managing, then select one
	 * 	2. Once selected, choose between the choices (book/print/approve-reject)
	 * 
	 * Feel free to redo this whole segment, it is very big and messy ;-;
	 * I think the change in status does not have any file changes?
	 * Receipt is missing as well
	 * 
	 * Booking function needs to affect the file and Applicant's typeofFlat, it also needs to 
	 * decrease the flatSupply in file + object
	 * */

	public void showMenuApplicant(Applicant user) {
		//Prints only 1 application? idk
			System.out.println(applicationManager.getApplicationsForUser(user));
	}

	//Missing book and print function?
	public void showMenuOfficer(HDBOfficer user) {

			int choice = -1;
			Scanner sc = new Scanner(System.in);

			do {
				int listSize = user.getManagedProject().size();
				for (int i = 0; i < listSize; i++) {
					System.out.println((i + 1) + ". " + user.getManagedProject().get(i).getProjectName());
				}
				System.out.println((user.getManagedProject().size() + 1) + ". EXIT");
				choice = sc.nextInt(); // try-catch &

				if (1 <= choice && choice <= listSize) {
					Project selected = user.getManagedProject().get(choice-1);
					List<Application> list = applicationManager.getApplicationsForProject(selected);
					listSize = list.size();
					do {
						for (int i = 0; i < list.size(); i++) {
							System.out.println((i + 1) + ". \n" + list.get(i).toString());
						}
						System.out.println((list.size()+1) + ". EXIT");
						choice = sc.nextInt();

						if (1 <= choice && choice <= listSize) {
							// Choose to book or print
							Application application = list.get(choice-1);

							do {
								System.out.print(application + "\n");
								System.out.println("1. Book");
								System.out.println("2. Print Receipt");
								System.out.println("3. Previous");
								choice = sc.nextInt();

								switch (choice) {
								case 1:
									//Book function missing?
								case 2:
									//Print receipt?
								case 3:
									System.out.println("Going to Previous Page");
									break;
								default:
									System.out.println("Try again.");
								}
							} while (true);

						} else if (listSize+1 == choice) {
							System.out.println("Going to Previous Page");
							break;
						} else {
							System.out.println("Try again.");
						}

					} while (true);

				} else if (listSize+1 == choice) {
					System.out.println("Exitting Application Section");
					return;
				} else {
					System.out.println("Try again.");
				}

			} while (true);
	}

	//Copypasted from Officer just added switch choice of approve/reject
	public void showMenuManager(HDBManager user) {
			int choice = -1;
			Scanner sc = new Scanner(System.in);

			do {
				int listSize = user.getManagedProject().size();
				for (int i = 0; i < listSize; i++) {
					System.out.println((i + 1) + ". " + user.getManagedProject().get(i).getProjectName());
				}
				System.out.println((user.getManagedProject().size() + 1) + ". EXIT");
				choice = sc.nextInt(); // try-catch &

				if (1 <= choice && choice <= listSize) {
					Project selected = user.getManagedProject().get(choice-1);
					List<Application> list = applicationManager.getApplicationsForProject(selected);
					listSize = list.size();
					do {
						for (int i = 0; i < list.size(); i++) {
							System.out.println((i + 1) + ". \n" + list.get(i).toString());
						}
						System.out.println((list.size()+1) + ". EXIT");
						choice = sc.nextInt();

						if (1 <= choice && choice <= listSize) {
							// Choose to book or print
							Application application = list.get(choice-1);

							 do {
									System.out.print(application);
									System.out.println("1. Print Receipt");
									System.out.println("2. Approve");
									System.out.println("3. Reject");
									System.out.println("4. Previous");
									choice = sc.nextInt();

									switch (choice) {
									case 1:
										//Print receipt?
									case 2:
										//Approve -- Do we edit the Project file
										//Number of units for Type 2 decrease?
										applicationManager.approveApplication(application.getApplicant());
										System.out.println("Approved. Exitting");
										return;
									case 3:
										//Reject -- Missing?
										System.out.println("Rejected. Exitting");
										return;
									case 4:
										System.out.println("Going to Previous Page");
										break;
									default:
										System.out.println("Try again.");
									}
								} while (true);

						} else if (listSize+1 == choice) {
							System.out.println("Going to Previous Page");
							break;
						} else {
							System.out.println("Try again.");
						}

					} while (true);

				} else if (listSize+1 == choice) {
					System.out.println("Exitting Application Section");
					return;
				} else {
					System.out.println("Try again.");
				}

			} while (true);
	}
		
	
	
	/**
	 * @return the userManager
	 */
	public IUserManager getUserManager() {
		return userManager;
	}

	/**
	 * @return the projectManager
	 */
	public IProjectManager getProjectManager() {
		return projectManager;
	}

	/**
	 * @return the projectRegManager
	 */
	public ProjectRegistration getProjectRegManager() {
		return projectRegManager;
	}
	
}
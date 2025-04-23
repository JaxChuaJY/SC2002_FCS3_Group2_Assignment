package main;

import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

import application.Application;
import application.ApplicationManager;
import enquiry.Enquiry;
import enquiry.EnquiryManager;
import enums.ApplicationStatus;
import enums.FlatType;
import enums.MaritalStatus;
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
import user.User;
import user.UserManager;

public class BTOManagementSystem {
	private IUserManager userManager;
	private IProjectManager projectManager;
	private ProjectRegistration projectRegManager;
	private IApplicationManager applicationManager;
	private IFileHandler fileHandler;
	private EnquiryManager enquiryManager;

	BTOManagementSystem() {
		fileHandler = new FileHandler();
		userManager = new UserManager(fileHandler);
		projectManager = new ProjectManager(fileHandler, userManager);
		projectRegManager = new ProjectRegistration(userManager, projectManager);
		applicationManager = new ApplicationManager(projectManager, userManager, fileHandler);
		enquiryManager = new EnquiryManager();
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

	public void login() {
		do {
			userManager.login();
		} while (userManager.getcurrentUser() == null);
	}

	public void logout() {
		userManager.logout();
	}

	public void changePassword() {
		userManager.changePassword();
		logout();

		try {
			userManager.reIntialise();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void showMenu() {
		userManager.getcurrentUser().showMenu(this);
	}

	// -----Project Section
	/*
	 * Feel free to do it as you like
	 */

	public void showProjMenu() {
		
		Scanner sc = new Scanner(System.in);
		if (userManager.getcurrentUser() instanceof Applicant || userManager.getcurrentUser() instanceof HDBOfficer) {
			while (true) {
				System.out.println("\n=== Applicant/Officer Projects ===\n");
				projectManager.viewAllProj(userManager.getcurrentUser());
				System.out.print("\n");
				System.out.print("Enter project's name you would like to view details (enter -1 to exit):");
				String choice = sc.nextLine();
				
				if (choice.equalsIgnoreCase("-1")) {
					break;
				}
				else if (projectManager.getProject(choice) != null) {
					Project proj = projectManager.getProject(choice);
					System.out.print(proj.toString());
					System.out.print("\nDo you want to apply for this project? (Y/N): ");
					String applyChoice = sc.nextLine();
					if (applyChoice.equalsIgnoreCase("y")) {
						System.out.print("\nEnter room type (2-room or 3-room)");
						FlatType flatChoice = FlatType.fromString(sc.nextLine());
						applicationManager.createApplication(userManager.getcurrentUser(), projectManager.getProject(choice), flatChoice);
						applicationManager.saveApplicationtoCSV();
					}
					else if (applyChoice.equalsIgnoreCase("n")){
						continue;
					}
					else {
						System.out.print("Invalid input");
						continue;
					}
				}
				else {
					System.out.print("\nProject not found");
				}
			}
		}
		
		else if (userManager.getcurrentUser() instanceof HDBManager) {
			while (true) {
				System.out.print("\n=== Manager Project Menu ===\n");
				System.out.print("1. View all Projects\n"); 
				System.out.print("2. View Filtered list of Projects\n"); 
				System.out.print("3. Create new Project\n");
				System.out.print("4. Delete a Project\n");
				System.out.print("5. Edit Project Details\n");
				System.out.print("6. Toggle Visibility for a Project\n");
				System.out.print("7. Exit\n");
				int choice = sc.nextInt();
				switch (choice) {
				case 1:
					System.out.println("\n=== All Projects List ===\n");
					projectManager.viewAllProj(userManager.getcurrentUser());
					System.out.print("\n");
					System.out.print("Enter project's name you would like to view details (enter -1 to exit):");
					String str_input = sc.nextLine();
					
					if (str_input.equalsIgnoreCase("-1")) {
						break;
					}
					else if (projectManager.getProject(str_input) != null) {
						Project proj = projectManager.getProject(str_input);
						System.out.print(proj.toString());
						
						System.out.println("Input action [EDIT/TOGGLE_VIS/BACK]:");
						str_input = sc.next();
						
						while(true) {
							if (str_input.toUpperCase().equals("EDIT")) {
								//edit function;
								//need write into file
							} else if (str_input.toUpperCase().equals("TOGGLE_VIS")) {
								//toggle
								//need write into file
							} else if (str_input.toUpperCase().equals("BACK")){
								System.out.println("Going back...");
								break;
							}
						}
					}
					else {
						System.out.print("\nProject not found");
					}

				break;
			case 2:
				projectManager.filterView(userManager.getcurrentUser().getFilters(), (HDBManager) userManager.getcurrentUser());	
				break;
			case 3:
				projectManager.addProject((HDBManager)userManager.getcurrentUser());
				break;
			case 4:
				System.out.print("Enter Project name: ");
				projectManager.removeProject(sc.nextLine());
				break;
			case 5:
				System.out.print("Enter Project you would like to edit: ");
				Project proj = projectManager.getProject(sc.nextLine());
				if (proj != null){
					projectManager.editProject(proj);
					break;
				}
				else{
					System.out.print("\nProject not found");
					break;
				}
			case 6:
				System.out.print("Enter Project you would like to Toggle Visibility: ");
				proj = projectManager.getProject(sc.nextLine());
				if (proj != null){
					projectManager.toggleVisibility(proj);
					break;
				}
				else{
					System.out.print("\nProject not found");
					break;
				}
			case 7:
				return;
			default:
				System.out.println("Invalid input");
				break;
				}
			}
		}
	}

	// -----Register Section
	public void showRegMenu() {
		if (userManager.getcurrentUser() instanceof HDBOfficer) {
			regSection_Officer((HDBOfficer) userManager.getcurrentUser());
		} else if (userManager.getcurrentUser() instanceof HDBManager) {
			regSection_Manager((HDBManager) userManager.getcurrentUser());
		}
	}

	public void regSection_Officer(HDBOfficer user) {

		Scanner sc = new Scanner(System.in);
		int choice = -1;

		do {
			System.out.println("**--Officer Registration Page");
			System.out.println("1. Register for Project");
			System.out.println("2. View Registration Status");
			System.out.println("3. Exit");
			choice = sc.nextInt();

			switch (choice) {
			case 1:
				do {
					List<Project> filteredList = projectManager.getFilteredList_Register(userManager.getcurrentUser());
					filteredList = projectRegManager.getNonRegList(filteredList, user);

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
						projectRegManager.register(selected, user);
						return;

					} else if (choice == i) {
						return;
					} else {
						System.out.println("Invalid selection.");
					}
				} while (true);

			case 2: // Prints all registration form of user
				projectRegManager.printList_officer(user);
				break;

			case 3:
				System.out.println("Exiting Registration Page...");
				return;
			default:
				System.out.println("INVALID INPUT");
				break;

			}
		} while (choice != 3);

	}

	public void regSection_Manager(HDBManager user) {
		Scanner sc = new Scanner(System.in);
		int choice = -1;

		do {
			System.out.println("**--Manager Registration Page");
			System.out.println("1. Review pending registration applications");
			System.out.println("2. View all registrations");
			System.out.println("3. Exit");

			choice = sc.nextInt();

			switch (choice) {
			case 1:
				do {
					List<RegistrationForm> filteredList = projectRegManager.getFilteredList(user);

					int i = 1;
					for (RegistrationForm r : filteredList) {
						System.out.println(i++ + ". " + r);
					}
					System.out.println(i + ". Exit");
					System.out.println("Select a form by number: ");
					choice = sc.nextInt();

					if (choice >= 1 && choice <= filteredList.size()) {
						RegistrationForm selected = filteredList.get(choice - 1);

						do {
							System.out.println("Approve/Reject: " + selected.getProject().getProjectName() + " for "
									+ selected.getRegisteredBy().getName());

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

								// Add locally
								selected.getProject().addOfficer(selected.getRegisteredBy());
								selected.getRegisteredBy().addProject(selected.getProject());

								// Add in file
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
				projectRegManager.printList_ManagerFilter(user);	
				break;
			case 3:
				System.out.println("Exiting Registration Page...");
				return;
			default:
				System.out.println("INVALID INPUT");
			}

		} while (true);

	}

	// -----Application section
	public void showApplMenu() {
		if (userManager.getcurrentUser() instanceof HDBManager) {
			showMenuManager((HDBManager) userManager.getcurrentUser());
		} else if (userManager.getcurrentUser() instanceof HDBOfficer) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Continue as");
			System.out.println("1. HDB Officer");
			System.out.println("2. Applicant");
			int choice = sc.nextInt();
			if (choice == 1) {
				showMenuOfficer((HDBOfficer) userManager.getcurrentUser());
			} else if (choice == 2) {
				showMenuApplicant((Applicant) userManager.getcurrentUser());
			} else {
				System.out.println("Invalid Choice");
				return;
			}
		} else if (userManager.getcurrentUser() instanceof Applicant) {
			showMenuApplicant((Applicant) userManager.getcurrentUser());
		}

	}

	public void showMenuApplicant(Applicant user) {
		// Prints only 1 application? idk
		Scanner sc = new Scanner(System.in);

		Application application = user.getApplication();
		boolean hasApplication = application != null && !(application.getStatus() == ApplicationStatus.WITHDRAWN
				|| application.getStatus() == ApplicationStatus.UNSUCCESSFUL);

		System.out.println("**--Applicant Application Page");
		System.out.print("Existing Application: ");
		if (application == null) {
			System.out.println("NONE");
		} else {
			System.out.println(application);
			if (application.getStatus() == ApplicationStatus.BOOKED) {
				applicationManager.viewReceipt(application);
			}
			if (!hasApplication) {
				System.out.println("(Status: " + application.getStatus() + " — You may reapply)");
			}
		}
		System.out.println("1. " + (hasApplication ? "Withdraw Application" : "Apply for Project"));
		System.out.println("2. EXIT");

		int choice = sc.nextInt();
		switch (choice) {
	    case 1:
	        if (hasApplication) {
	            // Handle withdrawal
	            applicationManager.requestWithdrawal(application);
	            applicationManager.saveApplicationtoCSV();
	            System.out.println("Withdrawal Request Sent.");
	        } else {
	            List<Project> projectList = projectManager.getProjectList(user);
	            int listSize = projectList.size();
	            if (listSize == 0) {
	            	System.out.println("No Available Projects Now.");
	            	return;
	            }
	            System.out.println("-".repeat(27));
	            System.out.printf("%-2s %-20s %-15s %-12s\n", " ", "Project Name", "Neighborhood", "Closing Date");

				for (int i = 0; i < listSize; i++) {
					Project project = projectList.get(i);
					String formattedDate = project.getClosingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					System.out.printf("%-2s %-20s %-15s %-12s\n", (i + 1) + ".", project.getProjectName(),
							project.getNeighbourhood(), formattedDate);
				}
				System.out.println("-".repeat(27));
				System.out.println((listSize + 1) + ". EXIT");
				
				choice = sc.nextInt();
				
				if (1 <= choice && choice <= listSize) {
					Project selected = projectList.get(choice - 1);
					EnumMap<FlatType, SimpleEntry<Integer, Double>> flatSupply = selected.getFlatSupply();

				   // Filter flat types based on marital status and age
				    List<FlatType> viewableFlatTypes = flatSupply.keySet().stream()
				        .filter(ft -> user.getMaritalStatus().canView(Set.of(ft), user.getAge()))
				        .toList();

				    // Display the eligible flat types
				    for (int i = 0; i < viewableFlatTypes.size(); i++) {
				        FlatType type = viewableFlatTypes.get(i);
				        SimpleEntry<Integer, Double> info = flatSupply.get(type);
				        System.out.printf("%d. %s - Supply: %d, Cost: $%.2f%n", i + 1, type, info.getKey(), info.getValue());
				    }

				    System.out.print("Select a flat type: ");
				    int flatChoice = sc.nextInt();
				    if (flatChoice >= 1 && flatChoice <= viewableFlatTypes.size()) {
				        if (selected.getFlatSupply().containsKey(viewableFlatTypes.get(flatChoice - 1))) {
				        	if (selected.getFlatSupply().get(viewableFlatTypes.get(flatChoice - 1)).getKey() > 0) {
				                applicationManager.createApplication(user, selected, viewableFlatTypes.get(flatChoice - 1));
				                applicationManager.saveApplicationtoCSV();
				                System.out.println("Application sent. Please wait for approval.");
				            } else {
				                System.out.println("Sorry, no units available for the selected flat type.");
				            }
				        } else {
				            System.out.println("Invalid flat type selected.");
				        }
				    } else {
				        System.out.println("Invalid choice. Please select a valid flat type number.");
				    }
				}

			}
			break;
		case 2:
			System.out.println("Exiting application page.");
			break;
		default:
			System.out.println("Invalid option. Please try again.");
			break;
		}
	}
	
	public void showMenuOfficer(HDBOfficer user) {

		int choice = -1;
		Scanner sc = new Scanner(System.in);

		do {
			int listSize = user.getManagedProject().size();
			System.out.println("**--Officer Applications Page");
			for (int i = 0; i < listSize; i++) {
				System.out.println((i + 1) + ". " + user.getManagedProject().get(i).getProjectName());
			}
			System.out.println((user.getManagedProject().size() + 1) + ". EXIT");
			choice = sc.nextInt(); // try-catch &

			if (1 <= choice && choice <= listSize) {
				Project selected = user.getManagedProject().get(choice - 1);
				List<Application> list = applicationManager.getApplicationsForProject(selected).stream()
						.filter(application -> application.getStatus() == ApplicationStatus.SUCCESSFUL).toList();
				listSize = list.size();
				do {
					for (int i = 0; i < list.size(); i++) {
						System.out.println((i + 1) + ". " + list.get(i).toString());
					}
					System.out.println((list.size() + 1) + ". EXIT");
					choice = sc.nextInt();

					if (1 <= choice && choice <= listSize) {
						// Choose to book or print
						Application application = list.get(choice - 1);

						do {
							System.out.print(application + "\n");
							System.out.println("1. Book Flat");
							System.out.println("2. Print Receipt to TXT");
							System.out.println("3. Back to Application List");
							choice = sc.nextInt();

							switch (choice) {
							case 1:
								applicationManager.bookFlat(application);
								applicationManager.saveApplicationtoCSV();
								System.out.println("Flat Successfully booked. Exiting");
								break;
							case 2:
								applicationManager.writeReceipt(application);
								System.out.println("Receipt printed. Exiting");
								break;
							case 3:
								System.out.println("Going to Previous Page");
								return;
							default:
								System.out.println("Try again.");
							}
						} while (true);

					} else if (listSize + 1 == choice) {
						System.out.println("Going to Previous Page");
						break;
					} else {
						System.out.println("Try again.");
					}

				} while (true);

			} else if (listSize + 1 == choice) {
				System.out.println("Exiting Application Section");
				return;
			} else {
				System.out.println("Try again.");
			}

		} while (true);
	}

	public void showMenuManager(HDBManager user) {
		Scanner sc = new Scanner(System.in);
		int choice = -1;

		while (true) {
			List<Project> managedProjects = user.getManagedProject();
			int listSize = managedProjects.size();

			System.out.println("\n**--Manager Applications Page");
			for (int i = 0; i < listSize; i++) {
				System.out.println((i + 1) + ". " + managedProjects.get(i).getProjectName());
			}
			System.out.println((listSize + 1) + ". EXIT");

			choice = sc.nextInt();
			sc.nextLine();

			if (choice == listSize + 1) {
				System.out.println("Exiting Application Section");
				break;
			}

			if (choice < 1 || choice > listSize) {
				System.out.println("Try again.");
				continue;
			}

			while (true) {
				Project selectedProject = managedProjects.get(choice - 1);
				List<Application> applications = applicationManager.getApplicationsForProject(selectedProject).stream()
						.filter(a -> a.getStatus() == ApplicationStatus.PENDING
								|| a.getStatus() == ApplicationStatus.WITHDRAW_REQUEST)
						.toList();

				if (applications.isEmpty()) {
					System.out.println("No applications to process.");
					break;
				}
				System.out.println("\n--- Applications ---");
				for (int i = 0; i < applications.size(); i++) {
					System.out.println((i + 1) + ". " + applications.get(i));
				}
				System.out.println((applications.size() + 1) + ". BACK");

				choice = sc.nextInt();
				sc.nextLine();

				if (choice == applications.size() + 1) {
					System.out.println("Going back to project list.");
					break;
				}

				if (choice < 1 || choice > applications.size()) {
					System.out.println("Try again.");
					continue;
				}

				Application application = applications.get(choice - 1);

				while (true) {
					if (application.getStatus() == ApplicationStatus.WITHDRAW_REQUEST) {
						System.out.println("1. Approve Withdrawal");
						System.out.println("2. Reject Withdrawal");
						System.out.println("3. Back");
					} else {
						System.out.println("1. Approve Application");
						System.out.println("2. Reject Application");
						System.out.println("3. Back");
					}
					choice = sc.nextInt();
					sc.nextLine();

					if (choice == 1) {
						if (application.getStatus() == ApplicationStatus.WITHDRAW_REQUEST) {
							applicationManager.approveWithdrawal(application);
							System.out.println("Withdrawal approved.");
						} else {
							applicationManager.approveApplication(application);
							System.out.println("Application approved.");
						}
						applicationManager.saveApplicationtoCSV();
						break;
					} else if (choice == 2) {
						if (application.getStatus() == ApplicationStatus.WITHDRAW_REQUEST) {
							applicationManager.rejectWithdrawal(application);
							System.out.println("Withdrawal rejected.");
						} else {
							applicationManager.rejectApplication(application);
							System.out.println("Application rejected.");
						}
						applicationManager.saveApplicationtoCSV();
						break;
					} else if (choice == 3) {
						System.out.println("Returning to application list.");
						break;
					} else {
						System.out.println("Try again.");
					}

				}
			}
		}
	}

	// -----Enquiry Section
	public void showEnquiryMenu() {
		if (userManager.getcurrentUser() instanceof HDBManager) {
			showEnquiryManager((HDBManager) userManager.getcurrentUser());
		} else if (userManager.getcurrentUser() instanceof HDBOfficer) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Continue as");
			System.out.println("1. HDB Officer");
			System.out.println("2. Applicant");
			int choice = sc.nextInt();
			if (choice == 1) {
				showEnquiryOfficer((HDBOfficer) userManager.getcurrentUser());
			} else if (choice == 2) {
				showEnquiryApplicant((Applicant) userManager.getcurrentUser());
			} else {
				System.out.println("Invalid Choice");
				return;
			}
		}
	}

	public void showEnquiryApplicant(Applicant user) {

		Scanner sc = new Scanner(System.in);
		// View own enquires
		// Create enquiry
		System.out.println("**--Applicant Enquiry Page");
		System.out.println("1. View enquries");
		System.out.println("2. Create enquiry");
		System.out.println("3. EXIT");

		int choice = sc.nextInt(); // try-catch for wrong input type (string/char/etc.)

		while (true) {
			switch (choice) {
			case 1:
				System.out.println("-----All enquries made by user-----");
				List<Enquiry> enquiryList = enquiryManager.getEnquiry_filterSend(user);

				for (int i = 0; i < enquiryList.size(); i++) {
					System.out.println("--------------------------");
					System.out.println((i + 1) + ". " + enquiryList.get(i).getProject() + "\nMessage:"
							+ enquiryList.get(i).getMessage());
					System.out.println("--------------------------");
				}

				System.out.println((enquiryList.size() + 1) + ". EXIT");

				choice = sc.nextInt();

				if (choice == (enquiryList.size() + 1)) {
					System.out.println("Exiting...");
					return;
				} else if (choice < enquiryList.size() - 1 && choice > 0) {
					Enquiry selected = enquiryList.get(choice);
					System.out.print(selected);
					break;
				} else {
					System.out.println("Bad input");
				}

			case 2:
				System.out.println("-----Create Enquiry (Select Project)-----");
				System.out.println("Select Project");
				// I do not know if project need filter here or not
				List<Project> projectList = projectManager.getProjectList(user);
				int listSize = projectList.size();

				if (listSize == 0) {
					System.out.println("No Available Projects Now.");
					return;
				}
				System.out.println("-".repeat(27));
				System.out.printf("%-2s %-20s %-15s %-12s\n", " ", "Project Name", "Neighborhood", "Closing Date");

				for (int i = 0; i < listSize; i++) {
					Project project = projectList.get(i);
					String formattedDate = project.getClosingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					System.out.printf("%-2s %-20s %-15s %-12s\n", (i + 1) + ".", project.getProjectName(),
							project.getNeighbourhood(), formattedDate);
				}
				System.out.println("-".repeat(27));
				System.out.println((listSize + 1) + ". EXIT");
				choice = sc.nextInt();

				if (1 <= choice && choice <= listSize) {
					Project selected = projectList.get(choice - 1);
					System.out.print("Message: ");
					String message = sc.nextLine();
					enquiryManager.addEnquiry(user, selected, message);
					System.out.println("Enquiry made!");

				} else {
					System.out.println("Invalid Project selection");
				}
				break;
			case 3:
				System.out.println("Exiting...");
				return;
			default:
				System.out.println("Invalid input, try again.");
				break;
			}

		}
	}

	public void showEnquiryOfficer(HDBOfficer user) {

		Scanner sc = new Scanner(System.in);

		System.out.println("**--Officer Page");
		System.out.println("Pick a project to view");

		while (true) {
			for (int i = 0; i < user.getManagedProject().size(); i++) {
				System.out.println((i + 1) + ". " + user.getManagedProject().get(i).getProjectName());
			}
			System.out.println((user.getManagedProject().size() + 1) + ". EXIT");
			int choice = sc.nextInt(); // try-catch

			while (true) {
				if (choice == (user.getManagedProject().size() + 1)) {
					System.out.println("Exiting...");
					return;
				} else if (1 <= choice && choice <= user.getManagedProject().size()) {

					Project selected = user.getManagedProject().get(choice);
					List<Enquiry> list = enquiryManager.getEnquiryNOReply(selected);

					if (list.isEmpty()) {
						break;
					} else {
						// Select a enquire to reply
						System.out.println("=====Select Enquiry to reply to=====");
						for (int i = 0; i < list.size(); i++) {
							System.out.println((i + 1) + ". By: " + list.get(i).getSender().getName() + "Message: "
									+ list.get(i).getMessage());
						}
						System.out.println((list.size() + 1) + ". EXIT");
						choice = sc.nextInt();

						while (true) {
							if (choice == (list.size() + 1)) {
								System.out.println("Exiting...");
								return;
							} else if (1 <= choice && choice <= list.size()) {
								System.out.println("Please type reply:");
								String message = sc.next();
								enquiryManager.replyToEnquiry(list.get(choice).getEnquiryId(), message);
								System.out.println("Reply made!");
								return;

							} else {
								System.out.println("Invalid input");
							}
						}

					}

				} else {
					System.out.println("Try again for input");
				}

			} // end of second while true
		} // end of first while true

	}

	// Copy pasted from Officer.
	public void showEnquiryManager(HDBManager user) {
		Scanner sc = new Scanner(System.in);

		System.out.println("**--Manager Page");
		System.out.println("Pick a project to view");
		
		List<Project> pList = projectManager.getProjectList(user);
		while (true) {
			for (int i = 0; i < pList.size(); i++) {
				System.out.println((i + 1) + ". " + pList.get(i).getProjectName());
			}
			System.out.println((pList.size() + 1) + ". EXIT");
			int choice = sc.nextInt(); // try-catch

			while (true) {
				if (choice == (pList.size() + 1)) {
					System.out.println("Exiting...");
					return;
				} else if (1 <= choice && choice <= pList.size()) {

					Project selected = pList.get(choice);
					List<Enquiry> list = enquiryManager.getEnquiryNOReply(selected);

					if (list.isEmpty()) {
						break;
					} else {
						// Select a enquire to reply
						System.out.println("=====Select Enquiry to reply to=====");
						for (int i = 0; i < list.size(); i++) {
							System.out.println((i + 1) + ". By: " + list.get(i).getSender().getName() + "Message: "
									+ list.get(i).getMessage());
						}
						System.out.println((list.size() + 1) + ". EXIT");
						choice = sc.nextInt();

						while (true) {
							if (choice == (list.size() + 1)) {
								System.out.println("Exiting...");
								return;
							} else if (1 <= choice && choice <= list.size()) {
								System.out.println("Please type reply:");
								String message = sc.next();
								enquiryManager.replyToEnquiry(list.get(choice).getEnquiryId(), message);
								System.out.println("Reply made!");
								return;

							} else {
								System.out.println("Invalid input");
							}
						}

					}

				} else {
					System.out.println("Try again for input");
				}

			} // end of second while true
		} // end of first while true
	}

	//-----Report
	public void showReportMenu() {
 		System.out.println("=====Application Report Filter=====");
 		
 		Scanner sc = new Scanner(System.in);
 		Optional<MaritalStatus> maritalStatus = Optional.empty();
 		Optional<FlatType> flatType = Optional.empty();
 		OptionalInt minAge = OptionalInt.empty();
 		OptionalInt maxAge = OptionalInt.empty();
 		Optional<String> projectName = Optional.empty();
 		String input;
 		try {
 			System.out.print("Marital Status (e.g. MARRIED/SINGLE or leave blank): ");
 			input = sc.nextLine().trim();
 			maritalStatus = input.isEmpty() ? Optional.empty() : Optional.of(MaritalStatus.valueOf(input.toUpperCase()));
 			
 			// Flat type
 			System.out.print("Flat Type (e.g. TWO_ROOM, THREE_ROOM or leave blank): ");
 			input = sc.nextLine().trim();
 			flatType = input.isEmpty() ? Optional.empty() : Optional.of(FlatType.valueOf(input.toUpperCase()));
 			
 			// Age range
 			System.out.print("Minimum age (or leave blank): ");
 			input = sc.nextLine().trim();
 			minAge = input.isEmpty() ? OptionalInt.empty() : OptionalInt.of(Integer.parseInt(input));
 			
 			System.out.print("Maximum age (or leave blank): ");
 			input = sc.nextLine().trim();
 			maxAge = input.isEmpty() ? OptionalInt.empty() : OptionalInt.of(Integer.parseInt(input));
 			
 			// Project name
 			System.out.print("Project Name (or leave blank): ");
 			input = sc.nextLine().trim();
 			projectName = input.isEmpty() ? Optional.empty() : Optional.of(input);
 			
 			List<String> report = applicationManager.generateReport(maritalStatus, flatType, minAge, maxAge, projectName);
 			System.out.println("\n=====Filtered Application Report=====");
 			report.forEach(System.out::println);
         } catch (Exception e) {
             System.out.println("Invalid filter.");
         }
 
 	}
	
	//-----Filter Settings
	public void showFilterMenu() {
		boolean manager = userManager.getcurrentUser() instanceof HDBManager ? true : false;
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("=====Filters=====");
			System.out.println("1. Change location");
			System.out.println("2. Filter flatTypes");
			if (manager) {
				System.out.println("3. See Projects Managed by others?");
				System.out.println("4. Exit");
			} else {
				System.out.println("3. Exit");
			}

			int choice = sc.nextInt();

			switch (choice) {
			case 1:
				System.out.println("Please input location:(Input 'None' if none)");
				userManager.getcurrentUser().getFilters().setLocation(sc.next());
				System.out.println("Location set!");
				break;
			case 2:
				System.out.println("Please input FlatType ('2-room'/'3-room')");
				// Check for invalid inputs!
				userManager.getcurrentUser().getFilters().setFlatType(FlatType.fromString(sc.next()));
				break;
			case 3:
				if (manager) {
					System.out.println("See projects managed by others? (y/n)");
					String input = sc.next();
					if (input.toLowerCase().equals("y")) {
						userManager.getcurrentUser().getFilters().setmanagerViewALL(true);
						break;
					} else if (input.toLowerCase().equals("n")) {
						userManager.getcurrentUser().getFilters().setmanagerViewALL(false);
						break;
					} else {
						System.out.println("Invalid input");
						break;
					}
				} else {
					System.out.println("Exitting");
					return;
				}

			}

		}
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

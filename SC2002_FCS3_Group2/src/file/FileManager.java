package file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import enums.ApplicationStatus;
import enums.FlatType;
import interfaces.IProjectManager;
import interfaces.IUserManager;
import project.Project;
import registration.RegistrationForm;
import user.Applicant;
import user.HDBManager;
import user.HDBOfficer;
import user.User;

public class FileManager {

	// Map that stores the userType + fileName
	private static Map<Class<? extends User>, String> userMap = Map.of(Applicant.class, "ApplicantList.csv",
			HDBManager.class, "ManagerList.csv", HDBOfficer.class, "OfficerList.csv");
	
	private static String directory = "data/";
	
	private static String projectFileName = "ProjectList.csv";
	private static String registrationFileName = "RegistrationList.csv";

	// ------User-related file functions
	// Function that read User CSV files
	public static List<User> readingALLUsers() {
		List<User> allUsers = new ArrayList<>();
		for (Map.Entry<Class<? extends User>, String> entry : userMap.entrySet()) {
			allUsers.addAll(readUsers(entry.getValue(), entry.getKey()));
		}
		return allUsers;
	}

	// Abstract for all user types
	public static <T extends User> List<User> readUsers(String filename, Class<T> clazz) {
		List<User> list = new ArrayList<User>();
		try (Scanner scanner = new Scanner(new File(directory + filename))) {
			scanner.useDelimiter(",");

			if (scanner.hasNextLine()) {
				scanner.nextLine(); // Read and discard the first row
			}

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] data = line.split(",");

				if (data.length < 5) {
					throw new Exception("Data dirty -- Missing data");
				}

				T user = clazz.getDeclaredConstructor().newInstance();
				user.setName(data[0]);
				user.setNric(data[1]);
				user.setAge(Integer.parseInt(data[2]));
				user.setMaritalStatus(data[3].toUpperCase());
				user.setPassword(data[4]);

				list.add(user);
			}
			// scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find file!");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Error in reading File! -- AGE conversion");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("General Error!");
			e.printStackTrace();
		}

		return list;

	}

	// ------Project-related file functions
	// [Incomplete, selling price?
	// General logic is the same as User
	public static List<Project> readProject(IUserManager ur) {
		List<Project> list = new ArrayList<Project>();
		try (Scanner scanner = new Scanner(new File(directory + projectFileName))) {
			scanner.useDelimiter(",");

			if (scanner.hasNextLine()) {
				scanner.nextLine(); // Read and discard the first row
			}

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] data = line.split(",");

				if (data.length < 5) {
					throw new Exception("Data dirty -- Missing data");
				}

				Project p = new Project();
				p.setProjectName(data[0]);
				p.setNeighbourhood(data[1]);
				p.setFlatSupply(Map.of(FlatType.TWO_ROOM, Integer.parseInt(data[3]), FlatType.THREE_ROOM,
						Integer.parseInt(data[6])));
				// Selling price for flatType MISSING

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy");
				p.setOpeningDate(LocalDate.parse(data[8], formatter));
				p.setClosingDate(LocalDate.parse(data[9], formatter));

				HDBManager m = (HDBManager) ur.searchUser_Type(HDBManager.class, data[10].replace("\"", ""));
				p.setManager(m);// 10
				m.addProject(p);

				p.setOfficerSlots(Integer.parseInt(data[11]));

				for (int i = 12; i < data.length; i++) {
					HDBOfficer o = (HDBOfficer) ur.searchUser_Type(HDBOfficer.class, data[i].replace("\"", ""));
					p.addOfficer(o);
					o.addProject(p);
				}
				list.add(p);
			}
			// scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find file!");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Error in reading File! -- AGE conversion");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("General Error!");
			e.printStackTrace();
		}

		return list;
	}

	public static List<RegistrationForm> readRegForms(IUserManager ur, IProjectManager pr) {
		List<RegistrationForm> list = new ArrayList<RegistrationForm>();

		try (Scanner sc = new Scanner(new File(directory + registrationFileName))) {
			sc.useDelimiter(",");

			if (sc.hasNextLine()) {
				sc.nextLine();
			}

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] data = line.split(",");

				RegistrationForm f = new RegistrationForm();
				f.setRegisteredBy((HDBOfficer) ur.searchUser_Type(HDBOfficer.class, data[0]));
				f.setProject(pr.getProject(data[1]));
				f.setStatus(data[2]);

				list.add(f);
			}

		} catch (FileNotFoundException e) {
			System.out.println("Unable to find file!");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Error in reading File! -- AGE conversion");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("General Error!");
			e.printStackTrace();
		}

		return list;
	}

	// ------Write files
	public static void writeFile_changePW(String pw, User user) {
		String filename = userMap.get(user.getClass());

		// Rewrite the whole file..., check if works
		List<String> lines = new ArrayList<>();

		try (Scanner scanner = new Scanner(new File(directory + filename))) {
			scanner.useDelimiter(",");

			lines.add(scanner.nextLine() + "\n");

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] data = line.split(",");

				// Does it contain the NRIC?
				if (data[1].equals(user.getNric())) {
					lines.add(data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + pw + "\n");
				} else {
					lines.add(data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4] + "\n");
				}
			}
			scanner.close();

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(directory + filename))) {
				for (String line : lines) {
					writer.write(line);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find file!");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Error in reading File! -- AGE conversion");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("General Error!");
			e.printStackTrace();
		}

	}
	// Throw when cannot write to file

	// **Make it generic to write for different files?

	public static void writeFile_newRegForm(Project p, HDBOfficer u) {

		String newRow = u.getName() + "," + p.getProjectName() + "," + ApplicationStatus.WAITING.toString();
		List<String> lines = new ArrayList<>();

		try (Scanner scanner = new Scanner(new File(directory + registrationFileName))) {
			scanner.useDelimiter(",");
			while (scanner.hasNextLine()) {
				lines.add(scanner.nextLine() + "\n");
			}

			lines.add(newRow);
			scanner.close();

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(directory + registrationFileName))) {
				for (String line : lines) {
					writer.write(line);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeFile_setRegForm(RegistrationForm f) {

		// Rewrite the whole file..., check if works
		List<String> lines = new ArrayList<>();

		try (Scanner scanner = new Scanner(new File(directory + registrationFileName))) {
			scanner.useDelimiter(",");

			lines.add(scanner.nextLine() + "\n");

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] data = line.split(",");

				// Does it contain the NRIC?
				if (data[0].equals(f.getRegisteredBy().getName()) && data[1].equals(f.getProject().getProjectName())) {
					lines.add(data[0] + "," + data[1] + "," + f.getStatus().toString() + "\n");
				} else {
					lines.add(data[0] + "," + data[1] + "," + data[2] + "\n");
				}
			}
			scanner.close();

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(directory + registrationFileName))) {
				for (String line : lines) {
					writer.write(line);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find file!");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Error in reading File! -- AGE conversion");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("General Error!");
			e.printStackTrace();
		}

	}

	public static void writeFile_addOfficer(RegistrationForm f) {
		// Rewrite the whole file..., check if works
		List<String> lines = new ArrayList<>();

		try (Scanner scanner = new Scanner(new File(directory + projectFileName))) {
			scanner.useDelimiter(",");

			lines.add(scanner.nextLine() + "\n");

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] data = line.split(",");

				// Does it contain the NRIC?
				if (data[0].equals(f.getProject().getProjectName())) {
					lines.add(line.substring(0, line.length()-1) + "," + f.getRegisteredBy().getName() + "\"\n");
				} else {
					lines.add(line);
				}
			}
			scanner.close();

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(directory + projectFileName))) {
				for (String line : lines) {
					writer.write(line);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find file!");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Error in reading File! -- AGE conversion");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("General Error!");
			e.printStackTrace();
		}
	}
}

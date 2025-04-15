package user;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import enums.MaritalStatus;
import interfaces.IFileHandler;
import interfaces.IUserManager;

public class UserManager implements IUserManager{
	private List<User> userList;
	private User currentUser;
	
	// Map that stores the userType + fileName for writeFile_changePW
	private static Map<Class<? extends User>, String> userMap = Map.of(Applicant.class, "ApplicantList.csv",
				HDBManager.class, "ManagerList.csv", HDBOfficer.class, "OfficerList.csv");
	private static String directory = "data/";
	
	private static final String[] USER_FILES = {"data/ApplicantList.csv","data/ManagerList.csv","data/OfficerList.csv"};//,"data/ManagerList.csv","data/OfficerList.csv"};
	private final IFileHandler fileHandler;
	
	public UserManager(IFileHandler fH){
		userList = new ArrayList<>();
		fileHandler = fH;
		loadUsersFromCSV();
		
	}
	
	private void loadUsersFromCSV() {
		for (String filePath : USER_FILES) {
			fileHandler.readFromFile(filePath).stream()
				.skip(1)
				.map(entry -> createUserFromFile(filePath, entry.split(",")))
				.filter(Objects::nonNull)
				.forEach(userList::add);
		}
	}
	
	private User createUserFromFile(String file, String[] values) {
		
	    String name = values[0];
	    String nric = values[1];
	    int age = Integer.valueOf(values[2]);
	    MaritalStatus status = MaritalStatus.valueOf(values[3].toUpperCase());
	    String password = values[4];

	    switch(file) {
	        case "data/ApplicantList.csv":
	            return new Applicant(name, nric, age, status, password);
	        case "data/ManagerList.csv":
	            return new HDBManager(name, nric, age, status, password);
	        case "data/OfficerList.csv":
	            return new HDBOfficer(name, nric, age, status, password);
	        default:
	            System.out.println("Invalid File");
	            return null;
	    }
	}
	
	public static void writeFile_changePW(User user) {
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
					lines.add(data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + user.getPassword() + "\n");
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
	
	public boolean login() {
		try  {
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter IC: (Case-sensitive)");
			String nric = sc.next();
			
			if (!FormatChecker.nricFormat(nric)) {
				throw new NumberFormatException("NRIC Format is incorrect");
				//Perhaps wrong Exception
			}
			
			System.out.println("Enter password:");
			String password = sc.next();
						
			
			currentUser = findUserLogin(nric, password);
			if (currentUser != null) {
				System.out.println("User found! Logging in...");
				return true;
			}else {
				System.out.println("Incorrect input, please try again!");
				return false;
			}
			
		} catch (NumberFormatException e) {
			System.out.println("ERROR --- NRIC Format is incorrect!");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return false;
	}
	
	public void logout() {
		currentUser = null;
	}
	
	public void printAllUser() {
		for (User user : userList) {
			System.out.println(user);
		}
	}
	
	public User findUserLogin(String ic, String pw) {
		for (User user : userList) {
			if (user.getNric().equals(ic) && user.getPassword().equals(pw)) {
				return user;
			}
		}
		return null;
	}
	
	public User getcurrentUser() {
		return currentUser;
	}
	
	public void changePassword() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please input new password");
		String pw = sc.next();
		currentUser.setPassword(pw);
		
		writeFile_changePW(currentUser);
		
	}

	public void setList(List<User> list) {
		this.userList = list;
	}
	
	public User searchUser_Type(Class<?> clazz, String name) throws Exception {
	    for (User u : userList) {
	        if (u.getClass().equals(clazz) && u.getName().equals(name)) {
	            return u;
	        }
	    }

	    throw new Exception("User does not exist in Repo " + name);
	}

	
	
	//REMOVE
	public void addUser(User u) {
		userList.add(u);
	}

	@Override
	public void reIntialise() {
		loadUsersFromCSV();
		
	}

	@Override
	public User getUser(String nric) {
		return userList.stream()
				.filter(entry -> entry.getNric().equals(nric.toUpperCase()))
				.findAny()
				.orElse(null);
	}

	

}

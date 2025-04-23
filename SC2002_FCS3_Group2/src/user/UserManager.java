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

/**
 * Manages user authentication, loading from CSV, and password updates.
 * <p>
 * Supports Applicants, HDB Officers, and HDB Managers.
 * Reads user data on initialization and provides login/logout functionality.
 * </p>
 */
public class UserManager implements IUserManager{
	/** In-memory list of all users loaded from files. */
	private List<User> userList;
    /** The currently authenticated user, or null if none. */
	private User currentUser;
	
	/**
     * Maps each User subclass to its corresponding CSV filename for password updates.
     */
	private static Map<Class<? extends User>, String> userMap = Map.of(Applicant.class, "ApplicantList.csv",
				HDBManager.class, "ManagerList.csv", HDBOfficer.class, "OfficerList.csv");
	
	/** Base directory for user CSV files. */
	private static String directory = "data/";
	
    /** Paths to all user CSV files for initial loading. */
	private static final String[] USER_FILES = {"data/ApplicantList.csv","data/ManagerList.csv","data/OfficerList.csv"};//,"data/ManagerList.csv","data/OfficerList.csv"};
	
	/** Handler for reading and writing CSV data. */
	private final IFileHandler fileHandler;
	
	/**
     * Constructs a UserManager and loads users from CSV files.
     *
     * @param fH the file handler to use for CSV operations
     */
	public UserManager(IFileHandler fH){
		userList = new ArrayList<>();
		fileHandler = fH;
		loadUsersFromCSV();
		
	}
	
	/**
     * Loads users from each CSV file into memory, skipping headers.
     */
	private void loadUsersFromCSV() {
		for (String filePath : USER_FILES) {
			fileHandler.readFromFile(filePath).stream()
				.skip(1)
				.map(entry -> createUserFromFile(filePath, entry.split(",")))
				.filter(Objects::nonNull)
				.forEach(userList::add);
		}
	}
	
	/**
     * Creates a User instance based on the CSV file path and parsed values.
     *
     * @param file   the CSV file path
     * @param values the CSV columns: name, NRIC, age, status, password
     * @return a new User subclass instance, or null if file unrecognized
     */
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

	/**
     * Updates the password for the given user in its CSV file.
     *
     * @param user the User whose password was changed
     */
	public static void writeFile_changePW(User user) {
		String filename = userMap.get(user.getClass());

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
	
	/**
     * Prompts console for NRIC and password and attempts to log in.
     *
     * @return true if login successful, false otherwise
     */
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
	
	/**
     * Logs out the current user, clearing the session.
     */
	public void logout() {
		currentUser = null;
	}
	
	/**
     * Prints all loaded users to the console for debugging.
     */
	public void printAllUser() {
		for (User user : userList) {
			System.out.println(user);
		}
	}
	
	/**
     * Finds a user matching the provided NRIC and password.
     *
     * @param ic the NRIC to match
     * @param pw the password to match
     * @return the User if found, or null otherwise
     */
	public User findUserLogin(String ic, String pw) {
		for (User user : userList) {
			if (user.getNric().equals(ic) && user.getPassword().equals(pw)) {
				return user;
			}
		}
		return null;
	}
	
	/**
     * @return the currently logged-in user, or null if none
     */
	public User getcurrentUser() {
		return currentUser;
	}
	
	/**
     * Prompts the current user to change their password and persists change.
     */
	public void changePassword() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please input new password");
		String pw = sc.next();
		currentUser.setPassword(pw);
		
		writeFile_changePW(currentUser);
		
	}

	/**
     * @param list sets the internal user list to the provided list
     */
	public void setList(List<User> list) {
		this.userList = list;
	}
	
	/**
     * Searches for a user of specific class and name.
     *
     * @param clazz the User subclass to match
     * @param name  the user's name to match
     * @return the matching User
     * @throws Exception if not found
     */
	public User searchUser_Type(Class<?> clazz, String name) throws Exception {
	    for (User u : userList) {
	        if (u.getClass().equals(clazz) && u.getName().equals(name)) {
	            return u;
	        }
	    }

	    throw new Exception("User does not exist in Repo " + name);
	}

	/**
     * Re-initializes user data by reloading from CSV.
     */
	@Override
	public void reIntialise() {
		loadUsersFromCSV();
		
	}

	/**
     * Retrieves a User by NRIC for external lookup.
     *
     * @param nric the NRIC to match
     * @return the User, or null if not found
     */
	@Override
	public User getUser(String nric) {
		return userList.stream()
				.filter(entry -> entry.getNric().equals(nric.toUpperCase()))
				.findAny()
				.orElse(null);
	}

	

}

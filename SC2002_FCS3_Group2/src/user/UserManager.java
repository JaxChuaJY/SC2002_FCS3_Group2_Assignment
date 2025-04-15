package user;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import interfaces.IUserManager;

public class UserManager implements IUserManager{
	private List<User> list;
	private User currentUser;
	
	// Map that stores the userType + fileName
		private static Map<Class<? extends User>, String> userMap = Map.of(Applicant.class, "ApplicantList.csv",
				HDBManager.class, "ManagerList.csv", HDBOfficer.class, "OfficerList.csv");
		private static String directory = "data/";
	
	public UserManager(){
		list = readingALLUsers();
	}
	
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
		for (User user : list) {
			System.out.println(user);
		}
	}
	
	public User findUserLogin(String ic, String pw) {
		for (User user : list) {
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
		this.list = list;
	}
	
	public User searchUser_Type(Class<?> clazz, String name) throws Exception {
	    for (User u : list) {
	        if (u.getClass().equals(clazz) && u.getName().equals(name)) {
	            return u;
	        }
	    }

	    throw new Exception("User does not exist in Repo " + name);
	}

	
	
	//REMOVE
	public void addUser(User u) {
		list.add(u);
	}

	@Override
	public void reIntialise() {
		list = readingALLUsers();
		
	}

	@Override
	public User getUser(String nric) {
		return list.stream()
				.filter(entry -> entry.getNric().equals(nric.toUpperCase()))
				.findAny()
				.orElse(null);
	}

	

}

package user;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import enums.MaritalStatus;
import main.FileHandler;

public class UserManager {
	
	private static final String[] USER_FILES = {"data/ApplicantList.csv"};//,"data/ManagerList.csv","data/OfficerList.csv"};
	private Scanner sc;

	private List<User> userList;
	
	public UserManager(){
		this.sc = new Scanner(System.in);
		this.userList = new ArrayList<>();
		
	}
	public void initializeFromCSV() {
		for (String filePath : USER_FILES) {
			FileHandler.readFromFile(filePath).stream()
				.skip(1)
				.map(entry -> {
					String[] values = entry.split(",");
						return createUserFromFile(filePath,values);
						})
				.filter(user -> user != null)
				.forEach(user -> userList.add(user));
		}
	}
	
	User createUserFromFile(String file, String[] values) {
		
	    String name = values[0];
	    String nric = values[1];
	    int age = Integer.valueOf(values[2]);
	    MaritalStatus status = MaritalStatus.valueOf(values[3].toUpperCase());
	    String password = values[4];

	    switch(file) {
	        case "data/ApplicantList.csv":
	            return new Applicant(name, nric, age, status, password);
	        /*case "data/ManagerList.csv":
	            return new HDBManager(name, nric, age, status, password);
	        case "data/OfficerList.csv":
	            return new HDBOfficer(name, nric, age, status, password);*/
	        default:
	            System.out.println("Invalid File");
	            return null;
	    }
	}
	public User loginUser() {
		try {
			
			System.out.print("Enter ID: ");
	        String nric = sc.nextLine().trim();
	
	        System.out.print("Enter password: ");
	        String password = sc.nextLine().trim();
			
			User user =  userList.stream()
				.filter(entry -> entry.login(nric, password))
				.findAny()
				.orElse(null);
			
			if (user == null) {
				System.out.println("Login failed: Invalid ID or password");
				return null;
		    }
			
		    return user;
		    
		} catch (Exception e) {
			return loginUser();
		}
	}
	
	public void changePassword(String nric, String password) {
		userList.stream()
			.filter(entry -> entry.getNric().equals(nric.toUpperCase()))
			.toList().getFirst().changePassword(password);
	}
	
	public User getUser(String nric) {
		return userList.stream()
				.filter(entry -> entry.getNric().equals(nric.toUpperCase()))
				.findAny()
				.orElse(null);
	}
}

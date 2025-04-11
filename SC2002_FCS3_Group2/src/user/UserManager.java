package user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import enums.FlatType;
import enums.MaritalStatus;
import interfaces.IFileHandler;
import interfaces.IUserManager;
import main.FileHandler;

public class UserManager implements IUserManager{
	
	private static final String[] USER_FILES = {"data/ApplicantList.csv"};//,"data/ManagerList.csv","data/OfficerList.csv"};
	private final IFileHandler fileHandler;

	private List<User> userList;
	
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
	        /*case "data/ManagerList.csv":
	            return new HDBManager(name, nric, age, status, password);
	        case "data/OfficerList.csv":
	            return new HDBOfficer(name, nric, age, status, password);*/
	        default:
	            System.out.println("Invalid File");
	            return null;
	    }
	}
	
	@Override
	public User loginUser(String nric, String password) {
		
		User user =  userList.stream()
			.filter(entry -> entry.login(nric, password))
			.findAny()
			.orElse(null);
		
		if (user == null) {
			throw new IllegalArgumentException("Login failed: Invalid ID or password");
	    }
		
	    return user;
	}
	
	@Override
	public void changePassword(String nric, String password) {
		userList.stream()
			.filter(entry -> entry.getNric().equals(nric.toUpperCase()))
			.toList().getFirst().changePassword(password);
	}
	
	@Override
	public User getUser(String nric) {
		return userList.stream()
				.filter(entry -> entry.getNric().equals(nric.toUpperCase()))
				.findAny()
				.orElse(null);
	}
}



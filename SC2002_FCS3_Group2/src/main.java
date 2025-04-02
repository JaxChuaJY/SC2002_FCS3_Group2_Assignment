import java.util.*;

public class main {

	private static final String[] USER_FILES = {"data/ApplicantList.csv"};//,"data/ManagerList.csv","data/OfficerList.csv"};
    private static final String[] PROJECT_FILES = {"data/Projects.csv"};
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			List<User> userList = setUpUser();
			userList.forEach(user -> System.out.println(user.toString()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	//If it violates any design principles, please help to shift it.
	static List<User> setUpUser() {
		
		List<User> userList = new ArrayList<User>();
		
		for (String file : USER_FILES) {
			List<User> userGroup = FileHandler.readFromFile(file).stream()
									.skip(1)
									.map(entry -> {
										String[] values = entry.split(",");
										return createUserFromFile(file,values);
										})
									.filter(user -> user != null)
									.toList();
			userList.addAll(userGroup);
		}
		return userList;
		
	}
	
	//If it violates any design principles, please help to shift it.
	static User createUserFromFile(String file, String[] values) {
		
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
}

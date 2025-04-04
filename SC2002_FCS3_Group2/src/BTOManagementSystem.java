import java.util.List;

public class BTOManagementSystem {
	//private List<Project> projectList;
	private List<User> userList;
	private ApplicationManager applicationManager;
	
	BTOManagementSystem(){}
	
	public void initUser(String filePath) {
		List<User> userGroup = FileHandler.readFromFile(filePath).stream()
				.skip(1)
				.map(entry -> {
					String[] values = entry.split(",");
						return createUserFromFile(filePath,values);
						})
					.filter(user -> user != null)
					.toList();
		this.userList.addAll(userGroup);
	}
	
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
	public User loginUser(String nric, String password) {
		return userList.stream()
				.filter(entry -> entry.login(nric, password)==true)
				.findFirst()
				.orElse(null);
	}
}

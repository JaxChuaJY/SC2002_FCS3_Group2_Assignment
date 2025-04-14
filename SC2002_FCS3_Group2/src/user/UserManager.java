package user;

import java.util.List;
import java.util.Scanner;

import interfaces.IUserManager;

public class UserManager implements IUserManager{
	private List<User> list;
	private User currentUser;
	
	public UserManager(List<User> input){
		list = input;
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
	
	public String changePassword() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please input new password");
		String pw = sc.next();
		
		return pw;
		
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



}

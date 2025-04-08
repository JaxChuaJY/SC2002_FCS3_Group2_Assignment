package main;

import java.util.*;

public class main {

	private static final String[] USER_FILES = {"data/ApplicantList.csv"};//,"data/ManagerList.csv","data/OfficerList.csv"};
    private static final String[] PROJECT_FILES = {"data/Projects.csv"};
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			BTOManagementSystem system = new BTOManagementSystem();
			for (String file : USER_FILES) {
				system.initUser(file);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

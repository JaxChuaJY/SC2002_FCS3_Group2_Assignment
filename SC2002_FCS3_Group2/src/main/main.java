package main;

import java.util.*;

import user.User;

public class main {

	private static final String[] PROJECT_FILES = {"data/Projects.csv"};
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			BTOManagementSystem system = new BTOManagementSystem();
			system.startSystem();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

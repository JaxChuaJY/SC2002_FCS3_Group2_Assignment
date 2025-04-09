package main;

import java.util.Scanner;

import user.Applicant;
import user.User;
import user.UserManager;

public class MenuManager {

	private UserManager userManager;
    private User currentUser;
    private Scanner sc;

    public MenuManager(UserManager userManager, Scanner sc) {
        this.userManager = userManager;
        this.sc = sc;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void displayMenu() {
        if (currentUser instanceof Applicant) { // Assumes User has isAdmin()
        	showApplicantMenu();
        } //else if (currentUser instanceof HDBOfficer) {
        //} else if (currentUser instanceof HDBManager) {
        //}
    }

    private void showApplicantMenu() {
    }

    private void showManagerMenu() {
    }
    
    private void showOfficerMenu() {
    }
}

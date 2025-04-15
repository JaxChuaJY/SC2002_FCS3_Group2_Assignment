package file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import enums.ApplicationStatus;
import enums.FlatType;
import interfaces.IProjectManager;
import interfaces.IUserManager;
import project.Project;
import registration.RegistrationForm;
import user.Applicant;
import user.HDBManager;
import user.HDBOfficer;
import user.User;

public class FileManager {

	// Map that stores the userType + fileName
	private static Map<Class<? extends User>, String> userMap = Map.of(Applicant.class, "ApplicantList.csv",
			HDBManager.class, "ManagerList.csv", HDBOfficer.class, "OfficerList.csv");
	
	private static String directory = "data/";
	
	private static String projectFileName = "ProjectList.csv";
	private static String registrationFileName = "RegistrationList.csv";



	// ------Project-related file functions
	// [Incomplete, selling price?
	// General logic is the same as User


	// ------Write files


	// **Make it generic to write for different files?

}

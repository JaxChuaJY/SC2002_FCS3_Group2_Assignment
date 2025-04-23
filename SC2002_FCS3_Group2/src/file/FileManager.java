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

/**
 * Utility class for reading from and writing to CSV files for 
 * users, projects, and registrations in the BTO system.
 * <p>
 * All methods are static and operate on files under the base
 * {@code directory}. Filenames are mapped via {@link #userMap}.
 * </p>
 */
public class FileManager {

    /**
     * Maps each user class to its corresponding CSV filename.
     * <ul>
     *   <li>{@link Applicant} → "ApplicantList.csv"</li>
     *   <li>{@link HDBOfficer} → "OfficerList.csv"</li>
     *   <li>{@link HDBManager} → "ManagerList.csv"</li>
     * </ul>
     */
	private static Map<Class<? extends User>, String> userMap = Map.of(Applicant.class, "ApplicantList.csv",
			HDBManager.class, "ManagerList.csv", HDBOfficer.class, "OfficerList.csv");
	
	/**
     * Base directory where all data CSV files are stored.
     */
	private static String directory = "data/";
	
	/**
     * Filename for persisting the list of BTO projects.
     */
	private static String projectFileName = "ProjectList.csv";

	    /**
     * Filename for persisting HDB Officer registrations.
     */
	private static String registrationFileName = "RegistrationList.csv";



	// ------Project-related file functions
	// [Incomplete, selling price?
	// General logic is the same as User


	// ------Write files


	// **Make it generic to write for different files?

}

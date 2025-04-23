package enquiry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import interfaces.IProjectManager;
import interfaces.IUserManager;
import project.Project;
import user.User;

/**
 * Manages user enquiries for BTO projects, including persistence and retrieval.
 * <p>
 * Supports loading from CSV, exporting to CSV, adding new enquiries,
 * querying by sender or project, editing messages, and replying.
 * </p>
 */
public class EnquiryManager {
	/** List of all loaded enquiries. */
	private List<Enquiry> enquiryList;
    /** Formatter for parsing and formatting dates in dd/MM/yyyy pattern. */
	private static final DateTimeFormatter a = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    /** Create path for CSV file to be called up and loaded in */
	private static final String CSV_PATH = new File("data", "enquiries.csv").getPath();
    /**
     * Constructs a new EnquiryManager with an empty enquiry list.
     */
	public EnquiryManager(IProjectManager projectManager, IUserManager userManager) {
		this.projectManager = projectManager;
	    	this.userManager = userManager;
		enquiryList = new ArrayList<>();
	}

	// File-related functions
	/**	
     * Loads enquiries from a CSV file, creating Enquiry instances.
     * <p>
     * If the file does not exist, creates a new one with a header.
     * </p>
     *
     * @param filename       path to the CSV file
     * @param userManager    for resolving sender User objects
     * @param projectManager for resolving Project objects
     */
	public void loadFromCSV(String filename, IUserManager userManager, IProjectManager projectManager) {
		File file = new File(filename);

		if (!file.exists()) {
			System.out.println("CSV file not found. Creating an empty file.");
			try (FileWriter writer = new FileWriter(filename)) {
				writer.write("EnquiryId,Sender,Project,Message,Reply,DateCreated,DateReplied,Replied\n");
			} catch (IOException e) {
				System.out.println("Error creating CSV file: " + e.getMessage());
			}
			return; // keep the current enquiryList (not replaced)
		}

		enquiryList.clear();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine(); // skip header
			while ((line = br.readLine()) != null) {
				String[] parts = parseCSVLine(line);
				if (parts.length >= 7) {
					// String sender = parts[0];
					// String project = parts[1];
					//User u;
					try {
						//u = userManager.searchUser_Type(User.class, parts[0]);
						String senderNric = parts[0];
						Project p = projectManager.getProject(parts[1]);
						String message = parts[2];
						String reply = parts[3];
						LocalDate dateCreated = LocalDate.parse(parts[4], a);
						LocalDate dateReplied = parts[5].isEmpty() ? null : LocalDate.parse(parts[5], a);
						boolean replied = Boolean.parseBoolean(parts[6]);

						User sender = userManager.getUser(senderNric);
						if (sender == null) {
							System.out.println("User not found for NRIC: " + senderNric);
							continue;
						}	

						Enquiry e = new Enquiry(sender, p, message);
						e.setMessage(message);
						if (replied) {
							e.setReply(reply);
							e.setDateReplied(dateReplied);
						}
						e.setDateCreated(dateCreated);
						enquiryList.add(e);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
			System.out.println("Loaded enquiries from " + filename);
		} catch (IOException e) {
			System.out.println("Error reading CSV: " + e.getMessage());
		}
	}

	/**
     * Parses a single CSV line into its component fields, handling quoted tokens.
     *
     * @param line the raw CSV line
     * @return array of field values
     */
	private String[] parseCSVLine(String line) {
		List<String> tokens = new ArrayList<>();
		StringBuilder current = new StringBuilder();
		boolean inQuotes = false;

		for (int i = 0; i < line.length(); i++) {
			char ch = line.charAt(i);

			if (ch == '\"') {
				if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
					current.append('\"'); // escaped quote
					i++;
				} else {
					inQuotes = !inQuotes; // toggle quote mode
				}
			} else if (ch == ',' && !inQuotes) {
				tokens.add(current.toString());
				current.setLength(0);
			} else {
				current.append(ch);
			}
		}

		tokens.add(current.toString()); // add last field
		return tokens.toArray(new String[0]);
	}

   /**
     * Exports all enquiries to a CSV file, including headers.
     *
     * @param filename path to output CSV file
     */
	public void exportToCSV(String filename) {
		try (FileWriter writer = new FileWriter(filename)) {
			// Write CSV header
			writer.write("Sender,Project,Message,Reply,DateCreated,DateReplied,Replied\n");

			for (Enquiry e : enquiryList) {
				StringBuilder sb = new StringBuilder();
				sb.append(escapeCSV(e.getSender().getNric())).append(",");
				sb.append(escapeCSV(e.getProject().getProjectName())).append(",");
				sb.append(escapeCSV(e.getMessage())).append(",");
				sb.append(escapeCSV(e.getReply())).append(",");
				sb.append(e.getDateCreated()).append(",");
				sb.append(e.getDateReplied() == null ? "" : e.getDateReplied()).append(",");
				sb.append(e.isReplied());
				writer.write(sb.toString());
				writer.write("\n");
			}

			System.out.println("Enquiries exported to: " + filename);
		} catch (IOException e) {
			System.out.println("Error writing to CSV file: " + e.getMessage());
		}
	}

	/**
     * Escapes a field for CSV output, quoting if necessary.
     *
     * @param field the raw field value
     * @return safely escaped CSV field
     */
	private String escapeCSV(String field) {
		if (field == null)
			return "";
		if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
			field = field.replace("\"", "\"\"");
			return "\"" + field + "\"";
		}
		return field;
	}

	// End of file-related functions
	/**
     * Adds a new enquiry to the in-memory list.
     *
     * @param sender  the User who submits the enquiry
     * @param project the Project being enquired about
     * @param message the enquiry message
     */
	public void addEnquiry(User sender, Project project, String message) {
		Enquiry e = new Enquiry(sender, project, message);
		enquiryList.add(e);
		System.out.println("Date Created: " + e.getDateCreated());
		exportToCSV(CSV_PATH);
		// System.out.println("Enquiry added with ID: " + e.getEnquiryId());
	}

	/**
     * Prints all enquiries and their replies to console.
     */
	public void printAll() {
		if (enquiryList.isEmpty()) {
			System.out.println("No enquiries.");
		} else {
			for (int i = 0; i < enquiryList.size(); i++) {
				Enquiry e = enquiryList.get(i);
				System.out.println("[" + (i + 1) + "] " + e.getSender() + " - " + e.getMessage());
				System.out.println("Reply: " + e.getReply());
			}
		}
	}

	/**
     * Retrieves enquiries sent by a specific user.
     *
     * @param user the sender User
     * @return list of matching Enquiry objects
     */
	public List<Enquiry> getEnquiry_filterSend(User user) {
		return enquiryList.stream().filter(en -> en.getSender().equals(user.getName())).collect(Collectors.toList());
	}

	/**
     * Checks if an enquiry with given ID is deletable (not replied).
     *
     * @param id the enquiry ID to check
     * @return true if found and not replied, false otherwise
     */
	public boolean deleteCheck(int id) {
		for (int i = 0; i < enquiryList.size(); i++) {
			Enquiry e = enquiryList.get(i);
			if (e.getEnquiryId() == id) {
				return !e.isReplied(); // Only deletable if not replied
			}
		}
		return false; // Not found or already replied
	}

	/**
     * Adds an existing Enquiry instance to the list.
     *
     * @param enquiry the Enquiry to add
     */
	public void createEnquiryList(Enquiry enquiry) {
		enquiryList.add(enquiry);
	}

	/**
     * Retrieves the first enquiry for a specific project.
     *
     * @param project the Project to search
     * @return the Enquiry found or null if none
     */
	public Enquiry getEnquiryByProject(Project project) {
		for (Enquiry e : enquiryList) {
			if (e.getProject().getProjectName().equalsIgnoreCase(project.getProjectName())) {
				return e;
			}
		}
		return null;
	}

	/**
     * Retrieves all enquiries (replied or not) for a given project.
     *
     * @param project the Project to filter by
     * @return list of Enquiry for that project
     */
	public List<Enquiry> getEnquiryNOReply(Project project) {
		List<Enquiry> proj = new ArrayList<>();

		for (Enquiry e : enquiryList) {
			if (e.getProject().getProjectName().equalsIgnoreCase(project.getProjectName())) {
				proj.add(e);
			}
		}
		if (proj.isEmpty()) {
			System.out.println("No enquiries found for this project.");
		}

//        else {
//            System.out.println("Enquiries for project: " + project);
//            for (int i = 0; i < proj.size(); i++) {
//                Enquiry e = proj.get(i);
//                System.out.println( "[" + (i + 1) + "] " + e.getSender() + " - " + e.getMessage());
//                System.out.println("Reply: " + e.getReply());
//            }
//        }
		return proj;
	}

	/**
     * Edits the message of an enquiry if it has not been replied.
     *
     * @param id         the enquiry ID
     * @param newMessage the new message content
     */
	public void editEnquiry(int id, String newMessage) {
		for (Enquiry e : enquiryList) {
			if (e.getEnquiryId() == id) {
				if (e.isReplied() == true) {
					System.out.println("Sorry, Enquiry replied, unable to edit.");
				} else {
					e.setMessage(newMessage);
					System.out.println("Enquiry updated.");
					System.out.println("Date Created: " + e.getDateCreated());
					exportToCSV(CSV_PATH);
					return;
				}
			}
		}
		System.out.println("Enquiry not found.");
	}

	/**
     * Sets a reply for an enquiry by ID.
     *
     * @param id    the enquiry ID
     * @param reply the reply message to record
     */
	public void replyToEnquiry(int id, String reply) {
		for (Enquiry e : enquiryList) {
			if (e.getEnquiryId() == id) {
				e.setReply(reply);
				System.out.println("Reply saved.");
				exportToCSV(CSV_PATH);
				return;
			}
		}
		System.out.println("Enquiry not found.");
	}

}

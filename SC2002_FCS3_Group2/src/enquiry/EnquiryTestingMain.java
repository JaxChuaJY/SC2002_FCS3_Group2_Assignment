package enquiry;

import java.util.Scanner;

public class EnquiryTestingMain {
    public static void main(String[] args) {
        EnquiryManager manager = new EnquiryManager();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n====== Enquiry Manager ======");
            System.out.println("1. Add Enquiry");
            System.out.println("2. View All Enquiries");
            System.out.println("3. Edit Enquiry");
            System.out.println("4. Reply to Enquiry");
            System.out.println("5. View Enquiry by Project");
            System.out.println("6. Delete Enquiry");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("Sender Name: "); // i can getsender name
                    String sender = scanner.nextLine();
                    System.out.print("Project Name: "); // i can getproject name
                    String project = scanner.nextLine();
                    System.out.print("Message: ");
                    String message = scanner.nextLine();
                    //manager.addEnquiry(sender, project, message);
                    break;
                }

                case 2 -> {
                    manager.printAll();
                    break;
                }
                case 3 -> {
                    System.out.print("Enter Enquiry ID to edit: ");
                    int editId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter new message: ");
                    String newMessage = scanner.nextLine();
                    manager.editEnquiry(editId, newMessage);
                    break;
                }
                case 4 -> {
                    System.out.print("Enter Enquiry ID to reply to: ");
                    int replyId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter your reply: ");
                    String reply = scanner.nextLine();
                    manager.replyToEnquiry(replyId, reply);
                    break;
                }
                case 5 -> {
                    System.out.print("Enter project name to search: ");
                    String searchProject = scanner.nextLine();
                   // manager.viewEnquiry(searchProject);
                    break;
                }
                case 6 -> {
                    System.out.print("Enter Enquiry ID to delete: ");
                    int deleteId = Integer.parseInt(scanner.nextLine());
                   // manager.deleteEnquiry(deleteId);
                    break;
                }
                case 0 -> {
                    System.out.println("Exiting program. Goodbye!");
                    break;
                }
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } while (choice != 0);
    }
}
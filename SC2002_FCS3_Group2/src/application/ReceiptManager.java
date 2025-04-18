package application;

import interfaces.IFileHandler;
import interfaces.IReceiptManager;

import java.util.List;

public class ReceiptManager implements IReceiptManager {
    private final IFileHandler fileHandler;

    public ReceiptManager(IFileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    @Override
    public void writeReceipt(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null.");
        }
        String fileName = "receipt/receipt_" + application.getApplicant().getNric() + ".txt";
        StringBuilder receipt = new StringBuilder();
        receipt.append("==== HDB BTO Flat Booking Receipt ====\n")
                .append("Name: ").append(application.getApplicant().getName()).append("\n")
                .append("NRIC: ").append(application.getApplicant().getNric()).append("\n")
                .append("Age: ").append(application.getApplicant().getAge()).append("\n")
                .append("Marital Status: ").append(application.getApplicant().getMaritalStatus()).append("\n\n")
                .append("Flat Type Booked: ").append(application.getFlatType()).append("\n")
                .append("Project Name: ").append(application.getProject().getProjectName()).append("\n")
                .append("Location: ").append(application.getProject().getNeighbourhood()).append("\n")
                .append("Booking Status: ").append(application.getStatus()).append("\n");
        fileHandler.writeToFile(fileName, List.of(receipt.toString()));
    }

    @Override
    public void viewReceipt(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null.");
        }
        String fileName = "receipt/receipt_" + application.getApplicant().getNric() + ".txt";
        fileHandler.readFromFile(fileName).forEach(System.out::println);
    }
}
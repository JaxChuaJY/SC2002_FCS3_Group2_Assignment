package enquiry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import project.Project;
import user.User;

public class EnquiryManager {
    private List<Enquiry> enquiryList;

    public EnquiryManager() {
        enquiryList = new ArrayList<>();
    }

    public void addEnquiry(User sender, Project project, String message) {
        Enquiry e = new Enquiry(sender, project, message);
        enquiryList.add(e);
        System.out.println("Date Created: " + e.getDateCreated());
        //System.out.println("Enquiry added with ID: " + e.getEnquiryId());
    }

    public void printAll() {
        if (enquiryList.isEmpty()) {
            System.out.println("No enquiries.");
        } else {
            for (int i = 0; i < enquiryList.size(); i++) {
                Enquiry e = enquiryList.get(i);
                System.out.println( "[" + (i + 1) + "] " + e.getSender() + " - " + e.getMessage());
                System.out.println("Reply: " + e.getReply());
            }
        }
    }
    
    public List<Enquiry> getEnquiry_filterSend(User user) {
    	return enquiryList.stream().filter(en -> en.getSender().equals(user.getName()))
    			.collect(Collectors.toList());
    }

    public boolean deleteCheck(int id) {
        for (int i = 0; i < enquiryList.size(); i++) {
            Enquiry e = enquiryList.get(i);
            if (e.getEnquiryId() == id) {
                return !e.isReplied(); // Only deletable if not replied
            }
        }
        return false; // Not found or already replied
    }

    public void createEnquiryList(Enquiry enquiry) {
        enquiryList.add(enquiry);
    }

    public Enquiry getEnquiryByProject(Project project) {
        for (Enquiry e : enquiryList) {
            if (e.getProject().getProjectName().equalsIgnoreCase(project.getProjectName())) {
                return e;
            }
        }
        return null;
    }

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

    public void editEnquiry(int id, String newMessage) {
        for (Enquiry e : enquiryList) {
            if (e.getEnquiryId() == id) {
                if (e.isReplied()== true) {
                    System.out.println("Sorry, Enquiry replied, unable to edit.");
                }
                else {
                    e.setMessage(newMessage);
                    System.out.println("Enquiry updated.");
                    System.out.println("Date Created: " + e.getDateCreated());
                    return;
                }
            }
        }
        System.out.println("Enquiry not found.");
    }

    public void replyToEnquiry(int id, String reply) {
        for (Enquiry e : enquiryList) {
            if (e.getEnquiryId() == id) {
                e.setReply(reply);
                System.out.println("Reply saved.");
                return;
            }
        }
        System.out.println("Enquiry not found.");
    }
    

}
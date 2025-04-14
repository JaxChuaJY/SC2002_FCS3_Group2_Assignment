package user;

import java.io.IOException;

public class FormatChecker {
	
	public static boolean isNumeric(String str) {
	    try {
	        Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
	
	
	public static boolean nricFormat(String nric)  {
			
		if (nric.length() != 9) {
			System.out.println("Incorrect length");
			return false;
			
		} else if (!Character.isAlphabetic(nric.charAt(0)) || 
		        !Character.isAlphabetic(nric.charAt(8)) || 
		        !isNumeric(nric.substring(1, 8))) {
			System.out.println("Invalid format");
		        return false;  
		    }
		
		
		return true;
	}
}

package user;

import java.io.IOException;

/**
 * Utility class for validating user input formats.
 * <p>
 * Provides methods to check numeric strings and verify NRIC format.
 * </p>
 */
public class FormatChecker {
    /**
     * Checks if a given string represents a valid integer.
     *
     * @param str the string to check
     * @return true if the string can be parsed as an integer, false otherwise
     */
	public static boolean isNumeric(String str) {
	    try {
	        Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
	
    /**
     * Validates the format of an NRIC string.
     * <p>
     * A valid NRIC must be exactly 9 characters long,
     * start and end with a letter, and contain only digits in between.
     * </p>
     *
     * @param nric the NRIC string to validate
     * @return true if the NRIC matches the expected format, false otherwise
     */
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

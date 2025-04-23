package enums;

/**
 * Types of flats available for BTO application.
 */
public enum FlatType {
	TWO_ROOM,
	THREE_ROOM;
	/**
     * Converts a user‐supplied string into the corresponding {@link FlatType}.
     *
     * @param input the string representation of the flat type (e.g. "2-room", "three-room")
     * @return the matching FlatType constant
     * @throws IllegalArgumentException if input is null, empty, or does not match any FlatType
     */
	public static FlatType fromString(String input) {
	    if (input == null || input.trim().isEmpty()) {
	        throw new IllegalArgumentException("Flat type string cannot be null or empty");
	    }

	    // Normalize the input string: remove spaces, replace hyphens with underscores, and make it uppercase
	    String normalized = input.trim().toUpperCase().replace("-", "_").replace("2", "TWO").replace("3", "THREE");

	    // Convert to enum constant if possible
	    try {
	        return FlatType.valueOf(normalized);
	    } catch (IllegalArgumentException e) {
	        throw new IllegalArgumentException("Unknown flat type: " + input, e);
	    }
	}

	/**
     * Returns a user-friendly display string for this flat type.
     *
     * @return flat type string with hyphens and numeric values (e.g. "2-room", "3-room")
     */
    public String toDisplayString() {
        return name().replace("_", "-").replace("TWO", "2").replace("THREE", "3");
    }
}
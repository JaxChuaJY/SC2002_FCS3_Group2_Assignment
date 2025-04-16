package enums;

public enum FlatType {
	TWO_ROOM,
	THREE_ROOM;
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
    public String toDisplayString() {
        return name().replace("_", "-").replace("TWO", "2").replace("THREE", "3") + " Room";
    }
}
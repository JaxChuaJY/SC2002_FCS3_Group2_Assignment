package project;

import enums.FlatType;

/**
 * Utility class for converting string representations into {@link FlatType} enums.
 * <p>
 * Normalizes input strings (e.g., "2_room", "3_room") and validates
 * against known flat types.
 * </p>
 */
public class FlatTypeConverter {
	
    /**
     * Converts a flat type string to its {@link FlatType} equivalent.
     *
     * @param flatTypeStr the raw string representing the flat type
     * @return the matching {@link FlatType}
     * @throws IllegalArgumentException if the input is null, empty, or unrecognized
     */
    public static FlatType convertToFlatType(String flatTypeStr) {
        if (flatTypeStr == null || flatTypeStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Flat type string cannot be null or empty");
        }

        String normalized = flatTypeStr.trim().toUpperCase();
        switch (normalized) {
            case "2_ROOM":
                normalized = "TWO_ROOM";
                break;
            case "3_ROOM":
                normalized = "THREE_ROOM";
                break;
            default:
                throw new IllegalArgumentException("Unknown flat type: " + flatTypeStr);
        }

        // Convert to enum
        try {
            return Enum.valueOf(FlatType.class, normalized);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid flat type: " + flatTypeStr, e);
        }
    }
}
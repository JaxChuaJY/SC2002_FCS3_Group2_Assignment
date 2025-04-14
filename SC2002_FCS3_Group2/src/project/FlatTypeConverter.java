package project;

import enums.FlatType;

public class FlatTypeConverter {
	
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
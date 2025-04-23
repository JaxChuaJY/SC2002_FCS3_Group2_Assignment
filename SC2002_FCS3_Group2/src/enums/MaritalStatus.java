package enums;

import java.util.Set;

/**
 * Marital status of an applicant, each with its own eligibility rule
 * for viewing certain flat types at a given age.
 */
public enum MaritalStatus {

    /**
     * Single applicants: must be at least 35 years old and only view 2-room flats.
     */
    SINGLE {
        @Override
        public boolean canView(Set<FlatType> set, int age) {
            return age >= 35 && set.contains(FlatType.TWO_ROOM);
        }
    },

    /**
     * Married applicants: must be at least 21 years old and may view
     * either 2-room or 3-room flats.
     */   
    MARRIED {
        @Override
        public boolean canView(Set<FlatType> set, int age) {
            return age >= 21 && 
                (set.contains(FlatType.TWO_ROOM) || set.contains(FlatType.THREE_ROOM));
        }
    };

    public abstract boolean canView(Set<FlatType> set, int age);
}
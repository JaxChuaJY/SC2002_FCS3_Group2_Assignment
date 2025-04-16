package enums;

import java.util.Set;

public enum MaritalStatus {
    SINGLE {
        @Override
        public boolean canView(Set<FlatType> set, int age) {
            return age >= 35 && set.contains(FlatType.TWO_ROOM);
        }
    },
    MARRIED {
        @Override
        public boolean canView(Set<FlatType> set, int age) {
            return age >= 21 && 
                (set.contains(FlatType.TWO_ROOM) || set.contains(FlatType.THREE_ROOM));
        }
    };

    public abstract boolean canView(Set<FlatType> set, int age);
}
package enums;

public enum MaritalStatus {
    SINGLE {
        @Override
        public boolean canView(FlatType flatType, int age) {
            return age >= 35 && flatType == FlatType.TWO_ROOM;
        }
    },
    MARRIED {
        @Override
        public boolean canView(FlatType flatType, int age) {
            return age >= 21 && 
                (flatType == FlatType.TWO_ROOM || flatType == FlatType.THREE_ROOM);
        }
    };

    public abstract boolean canView(FlatType flatType, int age);
}
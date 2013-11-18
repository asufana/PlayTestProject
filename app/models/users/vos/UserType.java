package models.users.vos;


public enum UserType {
    
    STAFF(1),
    ASSISTANT(2);
    
    private Integer intValue;
    
    UserType(final Integer intValue) {
        this.intValue = intValue;
    }
    
    public Integer intValue() {
        return intValue;
    }
    
    public static UserType valueOf(final Integer intValue) {
        for (final UserType d : values()) {
            if (d.intValue().equals(intValue)) {
                return d;
            }
        }
        return null;
    }
}

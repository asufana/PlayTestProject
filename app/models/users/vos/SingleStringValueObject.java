package models.users.vos;

import javax.persistence.*;

import playddd.models.*;

public abstract class SingleStringValueObject extends ValueObject {
    
    @Transient
    private final String value;
    
    public SingleStringValueObject(final String value) {
        this.value = value;
        isSatisfied(value);
    }
    
    public String value() {
        return value;
    }
    
    @Override
    public String toString() {
        return value();
    }
    
    public abstract void isSatisfied(String value);
    
}

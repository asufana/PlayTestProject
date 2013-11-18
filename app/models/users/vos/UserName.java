package models.users.vos;

import javax.persistence.*;

import playddd.models.*;
import playddd.util.*;

@Embeddable
public class UserName extends ValueObject {
    
    @Column(name = "userName", nullable = false)
    private final String value;
    
    public UserName(final String value) {
        new Valid(value).notNull();
        this.value = value;
    }
    
    public String value() {
        return value;
    }
    
    @Override
    public String toString() {
        return value();
    }
}

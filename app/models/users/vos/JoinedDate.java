package models.users.vos;

import javax.persistence.*;

import org.hibernate.annotations.*;
import org.joda.time.*;

import playddd.models.*;
import playddd.util.*;

@Embeddable
public class JoinedDate extends ValueObject<JoinedDate> {
    
    @Column(name = "joinedDate")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    private final DateTime value;
    
    public JoinedDate(final DateTime value) {
        new Valid(value).notNull();
        this.value = value;
    }
    
    public DateMidnight value() {
        return value.toDateMidnight();
    }
    
    public String format() {
        return value.toString("yyyy/MM/dd");
    }
    
    @Override
    public String toString() {
        return format();
    }
}

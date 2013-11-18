package models.users;

import javax.persistence.*;

import models.users.vos.*;
import playddd.models.*;
import playddd.util.*;

@Entity(name = "Users")
public class User extends EntityModels<User> {
    
    @Embedded
    private final UserName userName;
    
    @Enumerated
    private final UserType userType;
    
    @Embedded
    private final JoinedDate joinedDate;
    
    public User(final UserName userName,
            final UserType userType,
            final JoinedDate joinedDate) {
        this.userName = userName;
        this.userType = userType;
        this.joinedDate = joinedDate;
    }
    
    public UserName userName() {
        return userName;
    }
    
    public UserType userType() {
        return userType;
    }
    
    public JoinedDate joinedDate() {
        return joinedDate;
    }
    
    @Override
    public void isSatisfied() {
        new Valid(userName).notNull();
        new Valid(userType).notNull();
        new Valid(joinedDate).notNull();
    }
    
}
